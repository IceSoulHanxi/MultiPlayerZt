package com.ixnah.mpzt.utils;

import org.jackhuang.hmcl.util.function.ExceptionalSupplier;
import org.jackhuang.hmcl.util.io.HttpRequest;
import org.jackhuang.hmcl.util.io.IOUtils;
import org.jackhuang.hmcl.util.io.NetworkUtils;
import org.jackhuang.hmcl.util.io.ResponseCodeException;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.jackhuang.hmcl.util.io.NetworkUtils.createHttpConnection;
import static org.jackhuang.hmcl.util.io.NetworkUtils.encodeLocation;

public class HttpRequestHelper extends HttpRequest.HttpGetRequest {

    protected String url;
    protected String method;
    protected byte[] body;

    public HttpRequestHelper(String url, String method) {
        super(url);
        this.url = url;
        this.method = method;
    }

    @Override
    public HttpURLConnection createConnection() throws IOException {
        HttpURLConnection con = createHttpConnection(new URL(url));
        con.setRequestMethod(method);
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }
        return con;
    }

    @Override
    public String getString() throws IOException {
        return getStringWithRetry(() -> {
            HttpURLConnection conn = this.createConnection();
            conn.setUseCaches(false);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setInstanceFollowRedirects(false);

            if (body != null) {
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(body);
                }
            }

            conn = resolveConnection(conn, new ArrayList<>());

            URL url = new URL(this.url);
            if (responseCodeTester != null) {
                responseCodeTester.accept(url, conn.getResponseCode());
            } else {
                if (conn.getResponseCode() / 100 != 2) {
                    if (!ignoreHttpCode && !toleratedHttpCodes.contains(conn.getResponseCode())) {
                        try {
                            throw new ResponseCodeException(url, conn.getResponseCode(), NetworkUtils.readData(conn));
                        } catch (IOException e) {
                            throw new ResponseCodeException(url, conn.getResponseCode(), e);
                        }
                    }
                }
            }

            return IOUtils.readFullyAsString("gzip".equals(conn.getContentEncoding()) ? IOUtils.wrapFromGZip(conn.getInputStream()) : conn.getInputStream());
        }, retryTimes);
    }

    public static String getStringWithRetry(ExceptionalSupplier<String, IOException> supplier, int retryTimes) throws IOException {
        Throwable exception = null;
        for (int i = 0; i < retryTimes; i++) {
            try {
                return supplier.get();
            } catch (Throwable e) {
                exception = e;
            }
        }
        if (exception != null) {
            if (exception instanceof IOException) {
                throw (IOException) exception;
            } else {
                throw new IOException(exception);
            }
        }
        throw new IOException("retry 0");
    }

    public static HttpURLConnection resolveConnection(HttpURLConnection conn, List<String> redirects) throws IOException {
        int redirect = 0;

        while(true) {
            Map<String, List<String>> properties = conn.getRequestProperties();
            String method = conn.getRequestMethod();
            int code = conn.getResponseCode();
            if (code < 300 || code > 307 || code == 306 || code == 304) {
                return conn;
            }

            String newURL = conn.getHeaderField("Location");
            conn.disconnect();
            if (redirects != null) {
                redirects.add(newURL);
            }

            if (redirect > 20) {
                throw new IOException("Too much redirects");
            }

            HttpURLConnection redirected = (HttpURLConnection)(new URL(conn.getURL(), encodeLocation(newURL))).openConnection();
            properties.forEach((k, v) -> v.forEach((element) -> redirected.addRequestProperty(k, element)));
            redirected.setRequestMethod(method);
            conn = redirected;
            conn.setUseCaches(false);
            conn.setConnectTimeout(8000);
            conn.setReadTimeout(8000);
            conn.setInstanceFollowRedirects(false);
            ++redirect;
        }
    }
}
