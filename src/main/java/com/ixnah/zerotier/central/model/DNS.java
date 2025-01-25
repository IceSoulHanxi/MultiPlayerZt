package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class DNS {

    /**
     * Search domain to use for DNS records
     **/
    @SerializedName("domain")
    private String domain = null;

    /**
     * IP address of unicast DNS service
     **/
    @SerializedName("servers")
    private List<String> servers = null;

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public List<String> getServers() {
        return servers;
    }

    public void setServers(List<String> servers) {
        this.servers = servers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNS dns = (DNS) o;
        return Objects.equals(domain, dns.domain) && Objects.equals(servers, dns.servers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain, servers);
    }

    @Override
    public String toString() {
        return "DNS{" +
                "domain='" + domain + '\'' +
                ", servers=" + servers +
                '}';
    }
}
