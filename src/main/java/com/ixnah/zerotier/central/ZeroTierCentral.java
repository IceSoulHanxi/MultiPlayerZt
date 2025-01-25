package com.ixnah.zerotier.central;

import com.google.gson.reflect.TypeToken;
import com.ixnah.mpzt.utils.HttpRequestHelper;
import com.ixnah.zerotier.central.api.NetworkApi;
import com.ixnah.zerotier.central.api.NetworkMemberApi;
import com.ixnah.zerotier.central.api.UtilApi;
import com.ixnah.zerotier.central.model.Member;
import com.ixnah.zerotier.central.model.Network;
import com.ixnah.zerotier.central.model.Status;
import org.jackhuang.hmcl.util.io.HttpRequest;

import java.net.MalformedURLException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ZeroTierCentral implements NetworkApi, NetworkMemberApi, UtilApi {

    public static final String DEFAULT_BASE_URL = "https://my.zerotier.com/api/v1";
    public static final String DEFAULT_TOKEN_TYPE = "Bearer";

    private final String baseUrl;
    private final String tokenType;
    private volatile String token;

    public ZeroTierCentral(String token) {
        this(DEFAULT_BASE_URL, DEFAULT_TOKEN_TYPE, token);
    }

    public ZeroTierCentral(String baseUrl, String tokenType, String token) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        this.tokenType = tokenType;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public CompletableFuture<Void> deleteNetwork(String networkID) {
        return new HttpRequestHelper(baseUrl + "/network/" + networkID, "DELETE")
                .authorization(tokenType, token)
                .getStringAsync().thenAccept(s -> {});
    }

    @Override
    public CompletableFuture<Network> getNetworkByID(String networkID) {
        return HttpRequest.GET(baseUrl + "/network/" + networkID)
                .authorization(tokenType, token)
                .getJsonAsync(Network.class);
    }

    @Override
    public CompletableFuture<List<Network>> getNetworkList() {
        return HttpRequest.GET(baseUrl + "/network")
                .authorization(tokenType, token)
                .getJsonAsync(new TypeToken<List<Network>>() {});
    }

    @Override
    public CompletableFuture<Network> newNetwork(Network body) {
        try {
            return HttpRequest.POST(baseUrl + "/network").json(body)
                    .authorization(tokenType, token)
                    .getJsonAsync(Network.class);
        } catch (MalformedURLException e) {
            CompletableFuture<Network> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Network> updateNetwork(Network body, String networkID) {
        try {
            return HttpRequest.POST(baseUrl + "/network/" + networkID).json(body)
                    .authorization(tokenType, token)
                    .getJsonAsync(Network.class);
        } catch (MalformedURLException e) {
            CompletableFuture<Network> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Void> deleteNetworkMember(String networkID, String memberID) {
        return new HttpRequestHelper(baseUrl + "/network/" + networkID + "/member/" + memberID, "DELETE")
                .authorization(tokenType, token)
                .getStringAsync().thenAccept(s -> {});
    }

    @Override
    public CompletableFuture<Member> getNetworkMember(String networkID, String memberID) {
        return HttpRequest.GET(baseUrl + "/network/" + networkID + "/member/" + memberID)
                .authorization(tokenType, token)
                .getJsonAsync(Member.class);
    }

    @Override
    public CompletableFuture<List<Member>> getNetworkMemberList(String networkID) {
        return HttpRequest.GET(baseUrl + "/network/" + networkID + "/member")
                .authorization(tokenType, token)
                .getJsonAsync(new TypeToken<List<Member>>() {});
    }

    @Override
    public CompletableFuture<Member> updateNetworkMember(Member body, String networkID, String memberID) {
        try {
            return HttpRequest.POST(baseUrl + "/network/" + networkID + "/member/" + memberID).json(body)
                    .authorization(tokenType, token)
                    .getJsonAsync(Member.class);
        } catch (MalformedURLException e) {
            CompletableFuture<Member> future = new CompletableFuture<>();
            future.completeExceptionally(e);
            return future;
        }
    }

    @Override
    public CompletableFuture<Status> getStatus() {
        return HttpRequest.GET(baseUrl + "/status")
                .authorization(tokenType, token)
                .getJsonAsync(Status.class);
    }
}
