package com.ixnah.zerotier.central.api;

import com.ixnah.zerotier.central.model.Member;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * ZeroTier Central API
 *
 * <p>ZeroTier Central Network Management Portal API.<p>All API requests must have an API token header specified in the <code>Authorization: token xxxxx</code> format.  You can generate your API key by logging into <a href=\"https://my.zerotier.com\">ZeroTier Central</a> and creating a token on the Account page.</p><p>eg. <code>curl -X GET -H \"Authorization: token xxxxx\" https://api.zerotier.com/api/v1/network</code></p><p><h3>Rate Limiting</h3></p><p>The ZeroTier Central API implements rate limiting.  Paid users are limited to 100 requests per second.  Free users are limited to 20 requests per second.</p> <p> You can get the OpenAPI spec here as well: <code>https://docs.zerotier.com/api/central/ref-v1.json</code></p>
 *
 */
public interface NetworkMemberApi  {

    /**
     * Delete a network member
     *
     */
    CompletableFuture<Void> deleteNetworkMember(String networkID, String memberID);

    /**
     * Return an individual member on a network
     *
     */
    CompletableFuture<Member> getNetworkMember(String networkID, String memberID);

    /**
     * Returns a list of Members on the network.
     *
     */
    CompletableFuture<List<Member>> getNetworkMemberList(String networkID);

    /**
     * Modify a network member
     *
     */
    CompletableFuture<Member> updateNetworkMember(Member body, String networkID, String memberID);
}
