package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class MemberConfig {

    /**
     * Allow the member to be a bridge on the network
     **/
    @SerializedName("activeBridge")
    private Boolean activeBridge = null;

    /**
     * Is the member authorized on the network
     **/
    @SerializedName("authorized")
    private Boolean authorized = null;

    @SerializedName("capabilities")
    private List<Integer> capabilities = null;

    /**
     * Time the member was created or first tried to join the network
     **/
    @SerializedName("creationTime")
    private Long creationTime = null;

    /**
     * ID of the member node.  This is the 10 digit identifier that identifies a ZeroTier node.
     **/
    @SerializedName("id")
    private String id = null;

    /**
     * Public Key of the member's Identity
     **/
    @SerializedName("identity")
    private String identity = null;

    /**
     * List of assigned IP addresses
     **/
    @SerializedName("ipAssignments")
    private List<String> ipAssignments = null;

    /**
     * Time the member was authorized on the network
     **/
    @SerializedName("lastAuthorizedTime")
    private Long lastAuthorizedTime = null;

    /**
     * Time the member was deauthorized on the network
     **/
    @SerializedName("lastDeauthorizedTime")
    private Long lastDeauthorizedTime = null;

    /**
     * Exempt this member from the IP auto assignment pool on a Network
     **/
    @SerializedName("noAutoAssignIps")
    private Boolean noAutoAssignIps = null;

    /**
     * Member record revision count
     **/
    @SerializedName("revision")
    private Integer revision = null;

    /**
     * Allow the member to be authorized without OIDC/SSO
     **/
    @SerializedName("ssoExempt")
    private Boolean ssoExempt = null;

    /**
     * Array of 2 member tuples of tag [ID, tag value]
     **/
    @SerializedName("tags")
    private List<List<Object>> tags = null;

    /**
     * Major version of the client
     **/
    @SerializedName("vMajor")
    private Integer vMajor = null;

    /**
     * Minor version of the client
     **/
    @SerializedName("vMinor")
    private Integer vMinor = null;

    /**
     * Revision number of the client
     **/
    @SerializedName("vRev")
    private Integer vRev = null;

    /**
     * Protocol version of the client
     **/
    @SerializedName("vProto")
    private Integer vProto = null;

    public Boolean getActiveBridge() {
        return activeBridge;
    }

    public void setActiveBridge(Boolean activeBridge) {
        this.activeBridge = activeBridge;
    }

    public Boolean getAuthorized() {
        return authorized;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public List<Integer> getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(List<Integer> capabilities) {
        this.capabilities = capabilities;
    }

    public Long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Long creationTime) {
        this.creationTime = creationTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public List<String> getIpAssignments() {
        return ipAssignments;
    }

    public void setIpAssignments(List<String> ipAssignments) {
        this.ipAssignments = ipAssignments;
    }

    public Long getLastAuthorizedTime() {
        return lastAuthorizedTime;
    }

    public void setLastAuthorizedTime(Long lastAuthorizedTime) {
        this.lastAuthorizedTime = lastAuthorizedTime;
    }

    public Long getLastDeauthorizedTime() {
        return lastDeauthorizedTime;
    }

    public void setLastDeauthorizedTime(Long lastDeauthorizedTime) {
        this.lastDeauthorizedTime = lastDeauthorizedTime;
    }

    public Boolean getNoAutoAssignIps() {
        return noAutoAssignIps;
    }

    public void setNoAutoAssignIps(Boolean noAutoAssignIps) {
        this.noAutoAssignIps = noAutoAssignIps;
    }

    public Integer getRevision() {
        return revision;
    }

    public void setRevision(Integer revision) {
        this.revision = revision;
    }

    public Boolean getSsoExempt() {
        return ssoExempt;
    }

    public void setSsoExempt(Boolean ssoExempt) {
        this.ssoExempt = ssoExempt;
    }

    public List<List<Object>> getTags() {
        return tags;
    }

    public void setTags(List<List<Object>> tags) {
        this.tags = tags;
    }

    public Integer getvMajor() {
        return vMajor;
    }

    public void setvMajor(Integer vMajor) {
        this.vMajor = vMajor;
    }

    public Integer getvMinor() {
        return vMinor;
    }

    public void setvMinor(Integer vMinor) {
        this.vMinor = vMinor;
    }

    public Integer getvRev() {
        return vRev;
    }

    public void setvRev(Integer vRev) {
        this.vRev = vRev;
    }

    public Integer getvProto() {
        return vProto;
    }

    public void setvProto(Integer vProto) {
        this.vProto = vProto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberConfig that = (MemberConfig) o;
        return Objects.equals(activeBridge, that.activeBridge) && Objects.equals(authorized, that.authorized) && Objects.equals(capabilities, that.capabilities) && Objects.equals(creationTime, that.creationTime) && Objects.equals(id, that.id) && Objects.equals(identity, that.identity) && Objects.equals(ipAssignments, that.ipAssignments) && Objects.equals(lastAuthorizedTime, that.lastAuthorizedTime) && Objects.equals(lastDeauthorizedTime, that.lastDeauthorizedTime) && Objects.equals(noAutoAssignIps, that.noAutoAssignIps) && Objects.equals(revision, that.revision) && Objects.equals(ssoExempt, that.ssoExempt) && Objects.equals(tags, that.tags) && Objects.equals(vMajor, that.vMajor) && Objects.equals(vMinor, that.vMinor) && Objects.equals(vRev, that.vRev) && Objects.equals(vProto, that.vProto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeBridge, authorized, capabilities, creationTime, id, identity, ipAssignments, lastAuthorizedTime, lastDeauthorizedTime, noAutoAssignIps, revision, ssoExempt, tags, vMajor, vMinor, vRev, vProto);
    }

    @Override
    public String toString() {
        return "MemberConfig{" +
                "activeBridge=" + activeBridge +
                ", authorized=" + authorized +
                ", capabilities=" + capabilities +
                ", creationTime=" + creationTime +
                ", id='" + id + '\'' +
                ", identity='" + identity + '\'' +
                ", ipAssignments=" + ipAssignments +
                ", lastAuthorizedTime=" + lastAuthorizedTime +
                ", lastDeauthorizedTime=" + lastDeauthorizedTime +
                ", noAutoAssignIps=" + noAutoAssignIps +
                ", revision=" + revision +
                ", ssoExempt=" + ssoExempt +
                ", tags=" + tags +
                ", vMajor=" + vMajor +
                ", vMinor=" + vMinor +
                ", vRev=" + vRev +
                ", vProto=" + vProto +
                '}';
    }
}
