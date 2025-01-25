package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Member {

    /**
     * concatenation of network ID and member ID
     **/
    @SerializedName("id")
    private String id = null;

    @SerializedName("clock")
    private Long clock = null;

    @SerializedName("networkId")
    private String networkId = null;

    /**
     * ZeroTier ID of the member
     **/
    @SerializedName("nodeId")
    private String nodeId = null;

    @SerializedName("controllerId")
    private String controllerId = null;

    /**
     * Whether or not the member is hidden in the UI
     **/
    @SerializedName("hidden")
    private Boolean hidden = null;

    /**
     * User defined name of the member
     **/
    @SerializedName("name")
    private String name = null;

    /**
     * User defined description of the member
     **/
    @SerializedName("description")
    private String description = null;

    @SerializedName("config")
    private MemberConfig config = null;

    /**
     * Last seen time of the member (milliseconds since epoch).  Note: This data is considered ephemeral and may be reset to 0 at any time without warning.
     **/
    @SerializedName("lastOnline")
    private Long lastOnline = null;

    /**
     * Time the member last checked in with the network controller in milliseconds since epoch. Note: This data is considered ephemeral and may be reset to 0 at any time without warning.
     **/
    @SerializedName("lastSeen")
    private Long lastSeen = null;

    /**
     * IP address the member last spoke to the controller via (milliseconds since epoch).  Note: This data is considered ephemeral and may be reset to 0 at any time without warning.
     **/
    @SerializedName("physicalAddress")
    private String physicalAddress = null;

    /**
     * ZeroTier version the member is running
     **/
    @SerializedName("clientVersion")
    private String clientVersion = null;

    /**
     * ZeroTier protocol version
     **/
    @SerializedName("protocolVersion")
    private Integer protocolVersion = null;

    /**
     * Whether or not the client version is new enough to support the rules engine (1.4.0+)
     **/
    @SerializedName("supportsRulesEngine")
    private Boolean supportsRulesEngine = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getClock() {
        return clock;
    }

    public void setClock(Long clock) {
        this.clock = clock;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MemberConfig getConfig() {
        return config;
    }

    public void setConfig(MemberConfig config) {
        this.config = config;
    }

    public Long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public Long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public String getPhysicalAddress() {
        return physicalAddress;
    }

    public void setPhysicalAddress(String physicalAddress) {
        this.physicalAddress = physicalAddress;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Integer getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Integer protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Boolean getSupportsRulesEngine() {
        return supportsRulesEngine;
    }

    public void setSupportsRulesEngine(Boolean supportsRulesEngine) {
        this.supportsRulesEngine = supportsRulesEngine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id) && Objects.equals(clock, member.clock) && Objects.equals(networkId, member.networkId) && Objects.equals(nodeId, member.nodeId) && Objects.equals(controllerId, member.controllerId) && Objects.equals(hidden, member.hidden) && Objects.equals(name, member.name) && Objects.equals(description, member.description) && Objects.equals(config, member.config) && Objects.equals(lastOnline, member.lastOnline) && Objects.equals(lastSeen, member.lastSeen) && Objects.equals(physicalAddress, member.physicalAddress) && Objects.equals(clientVersion, member.clientVersion) && Objects.equals(protocolVersion, member.protocolVersion) && Objects.equals(supportsRulesEngine, member.supportsRulesEngine);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clock, networkId, nodeId, controllerId, hidden, name, description, config, lastOnline, lastSeen, physicalAddress, clientVersion, protocolVersion, supportsRulesEngine);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", clock=" + clock +
                ", networkId='" + networkId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", hidden=" + hidden +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", config=" + config +
                ", lastOnline=" + lastOnline +
                ", lastSeen=" + lastSeen +
                ", physicalAddress='" + physicalAddress + '\'' +
                ", clientVersion='" + clientVersion + '\'' +
                ", protocolVersion=" + protocolVersion +
                ", supportsRulesEngine=" + supportsRulesEngine +
                '}';
    }
}
