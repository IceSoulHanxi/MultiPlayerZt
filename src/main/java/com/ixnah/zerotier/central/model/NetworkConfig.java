package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NetworkConfig {

    /**
     * Network ID
     **/
    @SerializedName("id")
    private String id = null;

    /**
     * Time the network was created
     **/
    @SerializedName("creationTime")
    private Long creationTime = null;

    /**
     * Array of network capabilities
     **/
    @SerializedName("capabilities")
    private List<Object> capabilities = null;

    @SerializedName("dns")
    private DNS dns = null;

    /**
     * Enable broadcast packets on the network
     **/
    @SerializedName("enableBroadcast")
    private Boolean enableBroadcast = true;

    /**
     * Range of IP addresses for the auto assign pool
     **/
    @SerializedName("ipAssignmentPools")
    private List<IPRange> ipAssignmentPools = null;

    /**
     * Time the network was last modified
     **/
    @SerializedName("lastModified")
    private Long lastModified = null;

    /**
     * MTU to set on the client virtual network adapter
     **/
    @SerializedName("mtu")
    private Integer mtu = null; // 2800

    /**
     * Maximum number of recipients per multicast or broadcast. Warning - Setting this to 0 will disable IPv4 communication on your network!
     **/
    @SerializedName("multicastLimit")
    private Integer multicastLimit = null; // 32

    @SerializedName("name")
    private String name = null;

    /**
     * Whether or not the network is private.  If false, members will *NOT* need to be authorized to join.
     **/
    @SerializedName("private")
    private Boolean _private = true;

    @SerializedName("routes")
    private List<Route> routes = null;

    @SerializedName("rules")
    private List<Map<String, Object>> rules = null;

    @SerializedName("ssoConfig")
    private Map<String, Object> ssoConfig = null; // Collections.singletonMap("enable", false)

    @SerializedName("tags")
    private List<Object> tags = null;

    @SerializedName("v4AssignMode")
    private Map<String, Boolean> v4AssignMode = null; // Collections.singletonMap("zt", true)

    @SerializedName("v46ssignMode")
    private Map<String, Boolean> v6AssignMode = null;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public List<Object> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Object> capabilities) {
        this.capabilities = capabilities;
    }

    public DNS getDns() {
        return dns;
    }

    public void setDns(DNS dns) {
        this.dns = dns;
    }

    public Boolean getEnableBroadcast() {
        return enableBroadcast;
    }

    public void setEnableBroadcast(Boolean enableBroadcast) {
        this.enableBroadcast = enableBroadcast;
    }

    public List<IPRange> getIpAssignmentPools() {
        return ipAssignmentPools;
    }

    public void setIpAssignmentPools(List<IPRange> ipAssignmentPools) {
        this.ipAssignmentPools = ipAssignmentPools;
    }

    public Long getLastModified() {
        return lastModified;
    }

    public void setLastModified(Long lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getMtu() {
        return mtu;
    }

    public void setMtu(Integer mtu) {
        this.mtu = mtu;
    }

    public Integer getMulticastLimit() {
        return multicastLimit;
    }

    public void setMulticastLimit(Integer multicastLimit) {
        this.multicastLimit = multicastLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getPrivate() {
        return _private;
    }

    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }

    public List<Map<String, Object>> getRules() {
        return rules;
    }

    public void setRules(List<Map<String, Object>> rules) {
        this.rules = rules;
    }

    public Map<String, Object> getSsoConfig() {
        return ssoConfig;
    }

    public void setSsoConfig(Map<String, Object> ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    public List<Object> getTags() {
        return tags;
    }

    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    public Map<String, Boolean> getV4AssignMode() {
        return v4AssignMode;
    }

    public void setV4AssignMode(Map<String, Boolean> v4AssignMode) {
        this.v4AssignMode = v4AssignMode;
    }

    public Map<String, Boolean> getV6AssignMode() {
        return v6AssignMode;
    }

    public void setV6AssignMode(Map<String, Boolean> v6AssignMode) {
        this.v6AssignMode = v6AssignMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NetworkConfig that = (NetworkConfig) o;
        return Objects.equals(id, that.id) && Objects.equals(creationTime, that.creationTime) && Objects.equals(capabilities, that.capabilities) && Objects.equals(dns, that.dns) && Objects.equals(enableBroadcast, that.enableBroadcast) && Objects.equals(ipAssignmentPools, that.ipAssignmentPools) && Objects.equals(lastModified, that.lastModified) && Objects.equals(mtu, that.mtu) && Objects.equals(multicastLimit, that.multicastLimit) && Objects.equals(name, that.name) && Objects.equals(_private, that._private) && Objects.equals(routes, that.routes) && Objects.equals(rules, that.rules) && Objects.equals(ssoConfig, that.ssoConfig) && Objects.equals(tags, that.tags) && Objects.equals(v4AssignMode, that.v4AssignMode) && Objects.equals(v6AssignMode, that.v6AssignMode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creationTime, capabilities, dns, enableBroadcast, ipAssignmentPools, lastModified, mtu, multicastLimit, name, _private, routes, rules, ssoConfig, tags, v4AssignMode, v6AssignMode);
    }

    @Override
    public String toString() {
        return "NetworkConfig{" +
                "id='" + id + '\'' +
                ", creationTime=" + creationTime +
                ", capabilities=" + capabilities +
                ", dns=" + dns +
                ", enableBroadcast=" + enableBroadcast +
                ", ipAssignmentPools=" + ipAssignmentPools +
                ", lastModified=" + lastModified +
                ", mtu=" + mtu +
                ", multicastLimit=" + multicastLimit +
                ", name='" + name + '\'' +
                ", private=" + _private +
                ", routes=" + routes +
                ", rules=" + rules +
                ", ssoConfig=" + ssoConfig +
                ", tags=" + tags +
                ", v4AssignMode=" + v4AssignMode +
                ", v6AssignMode=" + v6AssignMode +
                '}';
    }
}
