package com.ixnah.zerotier;

import com.ixnah.zerotier.central.ZeroTierCentral;
import com.ixnah.zerotier.central.ZeroTierOidc;
import com.ixnah.zerotier.central.model.Member;
import com.ixnah.zerotier.central.model.Network;
import com.ixnah.zerotier.central.model.TokenResponse;

import java.util.List;

public class ZeroTierOidcServerTest {
    public static void main(String[] args) throws Exception {
        ZeroTierOidc oidcProxy = new ZeroTierOidc(8080);
        oidcProxy.start(60 * 1000);
        System.out.println(oidcProxy.getAuthUri());
        oidcProxy.waitFor();
        TokenResponse tokenResponse = oidcProxy.getToken();
        System.out.println(tokenResponse);
        ZeroTierCentral central = new ZeroTierCentral(tokenResponse.getAccessToken());
        List<Network> networks = central.getNetworkList().get();
        for (Network network : networks) {
            System.out.println(network);
            List<Member> members = central.getNetworkMemberList(network.getId()).get();
            for (Member member : members) {
                System.out.println(member);
            }
        }
    }
}
