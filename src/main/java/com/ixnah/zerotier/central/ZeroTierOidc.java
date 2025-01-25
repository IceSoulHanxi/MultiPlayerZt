package com.ixnah.zerotier.central;

import com.ixnah.mpzt.utils.HttpRequestHelper;
import com.ixnah.zerotier.central.model.OidcConfiguration;
import com.ixnah.zerotier.central.model.TokenResponse;
import org.jackhuang.hmcl.util.DigestUtils;
import org.jackhuang.hmcl.util.io.HttpRequest;
import org.jackhuang.hmcl.util.io.HttpServer;
import org.jackhuang.hmcl.util.io.IOUtils;
import org.jackhuang.hmcl.util.io.NetworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Proxy;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import static org.jackhuang.hmcl.util.Lang.thread;
import static org.jackhuang.hmcl.util.i18n.I18n.i18n;
import static org.jackhuang.hmcl.util.io.IOUtils.DEFAULT_BUFFER_SIZE;

public class ZeroTierOidc extends HttpServer {
    private static final Logger log = LoggerFactory.getLogger(ZeroTierOidc.class);

    public static final String DEFAULT_OIDC_CONFIGURATION_URL = "https://accounts.zerotier.com/realms/zerotier/.well-known/openid-configuration";
    public static final String DEFAULT_REDIRECT_URI = "https://my.zerotier.com/auth/callback";
    public static final String DEFAULT_CLIENT_ID = "zt-central";
    public static final String DEFAULT_SCOPE = "openid profile email offline_access";

    private final CompletableFuture<Void> future;
    private final String realRedirectUri;
    private final String clientId;
    private final OidcConfiguration oidcConfiguration;
    private final String codeVerifier;
    private volatile TokenResponse token;

    public ZeroTierOidc(int port) throws IOException {
        this(port, loadConfig(DEFAULT_OIDC_CONFIGURATION_URL), DEFAULT_REDIRECT_URI, DEFAULT_CLIENT_ID);
    }

    public ZeroTierOidc(int port, OidcConfiguration oidcConfiguration, String realRedirectUri, String clientId) {
        super(port);
        this.oidcConfiguration = oidcConfiguration;
        this.realRedirectUri = realRedirectUri;
        this.clientId = clientId;
        this.future = new CompletableFuture<>();
        this.codeVerifier = genCodeVerifier();

        addRoute(Method.GET, Pattern.compile("/\\$proxy"), this::onProxy);
        try {
            String path = new URI(realRedirectUri).getPath();
            addRoute(Method.GET, Pattern.compile(path), this::onCallback);
        } catch (URISyntaxException e) {
            log.error("Invalid redirect uri", e);
        }
    }

    private Response onCallback(Request request) throws IOException, ExecutionException, InterruptedException, TimeoutException {
        Map<String, String> query = request.getQuery();
        token = authorizationToken(query.get("code")).get(8, TimeUnit.SECONDS);
        future.complete(null);

        String html = IOUtils.readFullyAsString(HttpServer.class.getResourceAsStream("/assets/microsoft_auth.html"))
                .replace("%close-page%", i18n("account.methods.microsoft.close_page"));
        thread(() -> {
            try {
                Thread.sleep(1000);
                stop();
            } catch (InterruptedException e) {
                log.error("Failed to sleep for 1 second");
            }
        });
        return newFixedLengthResponse(Response.Status.OK, "text/html; charset=UTF-8", html);
    }

    private Response onProxy(Request request) throws IOException, URISyntaxException {
        String url = request.getQuery().get("url");
        if (url == null || url.isEmpty()) return badRequest();
        IHTTPSession realSession = request.getSession();
        IHTTPSession proxySession = (IHTTPSession) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{IHTTPSession.class}, (proxy, method, args) -> {
                    if (method.getName().equals("getUri")) {
                        return url;
                    }
                    return method.invoke(realSession, args);
                });
        return proxy(proxySession);
    }

    @Override
    public Response serve(IHTTPSession session) {
        Response response = super.serve(session);
        if (Response.Status.NOT_FOUND.equals(response.getStatus())) {
            try {
                response = proxy(session);
                // 如果是重定向则劫持到当前HttpServer
                String location = response.getHeader("location");
                if (location != null && location.startsWith(realRedirectUri)) {
                    String path = new URI(realRedirectUri).getPath();
                    int pathIndex = location.indexOf(path);
                    location = pathIndex == -1
                            ? location.replace(realRedirectUri, getRootUrl())
                            : getRootUrl() + location.substring(pathIndex);
                } else if (location != null) {
                    location = getRootUrl() + "/$proxy?url=" + URLEncoder.encode(location, "UTF-8");
                }
                if (location != null) {
                    response.addHeader("location", location);
                }
            } catch (IOException | URISyntaxException e) {
                log.error("Proxy error", e);
                return internalError();
            }
        }
        return response;
    }

    public Response proxy(IHTTPSession session) throws IOException, URISyntaxException {
        Method method = session.getMethod();
        HttpRequest request;
        String requestUri = session.getUri();
        String authUriStr = oidcConfiguration.getAuthorizationEndpoint();
        URI authUri = new URI(authUriStr);
        String authHost = authUriStr.replace(authUri.getRawPath(), "");
        String rootUrl = getRootUrl();
        requestUri = requestUri.replace(rootUrl, authHost);
        if (!requestUri.startsWith(authHost)) {
            requestUri = authHost + requestUri;
        }
        String params = session.getQueryParameterString();
        String url = params != null ? (requestUri + '?' + params) : requestUri;
        switch (method) {
            case GET:
                request = HttpRequest.GET(url);
                break;
            case POST:
                request = HttpRequest.POST(url);
                break;
            case DELETE:
            case PUT:
                request = new HttpRequestHelper(url, method.name());
                break;
            case OPTIONS:
                Response response = HttpServer.newFixedLengthResponse(Response.Status.OK, "text/plain", "");
                response.addHeader("Access-Control-Allow-Credentials", "true");
                response.addHeader("Access-Control-Allow-Headers", "*");
                response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
                response.addHeader("Access-Control-Allow-Origin", "*");
                response.addHeader("Access-Control-Max-Age", "3600");
                return response;
            default:
                return HttpServer.newFixedLengthResponse(Response.Status.METHOD_NOT_ALLOWED, "text/plain", "405 Method Not Allowed");
        }
        Map<String, String> headers = session.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String key = entry.getKey();
            if ("remote-addr".equalsIgnoreCase(key) || "http-client-ip".equalsIgnoreCase(key)
                    || "host".equalsIgnoreCase(key) || "accept-encoding".equalsIgnoreCase(key)) continue;
            request.header(key, entry.getValue());
        }
        URI redirectUri = new URI(realRedirectUri);
        request.header("host", authUri.getPort() != -1 ? authUri.getHost() + ":" + authUri.getPort() : authUri.getHost());
        request.header("referer", realRedirectUri.replace(redirectUri.getRawPath(), "/"));
        HttpURLConnection connection = request.createConnection();
        connection.setInstanceFollowRedirects(false);
        String requestLengthStr = headers.get("content-length");
        if (requestLengthStr != null && !requestLengthStr.isEmpty() && !"0".equals(requestLengthStr)) {
            long requestLength = Long.parseLong(requestLengthStr);
            connection.setDoOutput(true);
            byte[] buf = new byte[(int) Math.min(requestLength, DEFAULT_BUFFER_SIZE)];
            InputStream inputStream = session.getInputStream();
            try (OutputStream outputStream = connection.getOutputStream()) {
                while (requestLength > 0) {
                    int len = inputStream.read(buf, 0, (int) Math.min(requestLength, DEFAULT_BUFFER_SIZE));
                    outputStream.write(buf, 0, len);
                    requestLength -= len;
                }
            } catch (SocketTimeoutException e) {
                log.error("Proxy error", e);
                return newFixedLengthResponse(Response.Status.SERVICE_UNAVAILABLE, "text/plain", "504 Gateway Timeout");
            }
        }

        Response.Status status = Response.Status.lookup(connection.getResponseCode());
        if (status == null) {
            status = Response.Status.INTERNAL_ERROR;
        }
        int contentLength = connection.getContentLength();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (FileNotFoundException e) {
            return notFound();
        }
        String contentType = connection.getContentType();
        Response response = contentLength == -1
                ? newChunkedResponse(status, contentType, inputStream)
                : newFixedLengthResponse(status, contentType, inputStream, contentLength);
        if (status.getRequestStatus() / 100 == 2 && contentType.startsWith("text/") && contentLength < 50 * 1024 * 1024) {
            String body = IOUtils.readFullyAsStringWithClosing(inputStream);
            body = body.replace(authHost, rootUrl);
            response = newFixedLengthResponse(status, contentType, body);
        }
        for (Map.Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            String key = entry.getKey();
            if (key == null || "content-type".equalsIgnoreCase(key) || "content-encoding".equalsIgnoreCase(key)
                    || "content-length".equalsIgnoreCase(key)) continue;
            List<String> value = entry.getValue();
            if (value.isEmpty()) continue;
            StringJoiner retValue = new StringJoiner(",");
            value.forEach(retValue::add);
            response.addHeader(key, retValue.toString());
        }
        return response;
    }

    public String getAuthUri() {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("client_id", clientId);
        params.put("redirect_uri", realRedirectUri);
        params.put("response_type", "code");
        params.put("scope", DEFAULT_SCOPE);
        params.put("state", UUID.randomUUID().toString().replace("-", ""));
        params.put("code_challenge", getCodeChallengeS256(codeVerifier));
        params.put("code_challenge_method", "S256");
        params.put("response_mode", "query");
        String endpoint = oidcConfiguration.getAuthorizationEndpoint();
        try {
            URI authUri = new URI(endpoint);
            endpoint = getRootUrl() + authUri.getPath();
        } catch (URISyntaxException e) {
            log.error("Invalid auth uri", e);
        }
        return NetworkUtils.withQuery(endpoint, params);
    }

    public CompletableFuture<Void> getFuture() {
        return future;
    }

    public String getRealRedirectUri() {
        return realRedirectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public OidcConfiguration getOidcConfiguration() {
        return oidcConfiguration;
    }

    public String getCodeVerifier() {
        return codeVerifier;
    }

    public void waitFor() throws InterruptedException, ExecutionException {
        this.future.get();
    }

    public TokenResponse getToken() {
        return token;
    }

    public CompletableFuture<TokenResponse> refreshToken() {
        return refreshToken(token.getRefreshToken()).thenCompose(tokenResponse -> {
            token = tokenResponse;
            return CompletableFuture.completedFuture(tokenResponse);
        });
    }

    public CompletableFuture<TokenResponse> authorizationToken(String code) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", "authorization_code");
        form.put("redirect_uri", realRedirectUri);
        form.put("code", code);
        form.put("code_verifier", codeVerifier);
        form.put("client_id", clientId);
        try {
            return HttpRequest.POST(oidcConfiguration.getTokenEndpoint()).form(form).getJsonAsync(TokenResponse.class);
        } catch (MalformedURLException e) {
            log.error("Invalid token endpoint", e);
            CompletableFuture<TokenResponse> fail = new CompletableFuture<>();
            fail.completeExceptionally(e);
            return fail;
        }
    }

    public CompletableFuture<TokenResponse> refreshToken(String refreshToken) {
        Map<String, String> form = new LinkedHashMap<>();
        form.put("grant_type", "refresh_token");
        form.put("refresh_token", refreshToken);
        form.put("scope", DEFAULT_SCOPE);
        form.put("client_id", clientId);
        try {
            return HttpRequest.POST(oidcConfiguration.getTokenEndpoint()).form(form).getJsonAsync(TokenResponse.class);
        } catch (MalformedURLException e) {
            log.error("Invalid token endpoint", e);
            CompletableFuture<TokenResponse> fail = new CompletableFuture<>();
            fail.completeExceptionally(e);
            return fail;
        }
    }

    public static OidcConfiguration loadConfig(String url) throws IOException {
        return HttpRequest.GET(url).getJson(OidcConfiguration.class);
    }

    public static String genCodeVerifier() {
        SecureRandom sr = new SecureRandom();
        byte[] code = new byte[32];
        sr.nextBytes(code);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(code);
    }

    public static String getCodeChallengeS256(String verifier) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(DigestUtils.digest("SHA-256", verifier.getBytes(StandardCharsets.US_ASCII)));
    }
}
