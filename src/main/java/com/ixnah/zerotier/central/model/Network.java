package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Objects;

/**
 * Network object
 **/
public class Network {

    @SerializedName("id")
    private String id = null;

    @SerializedName("clock")
    private Long clock = null;

    @SerializedName("config")
    private NetworkConfig config = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("rulesSource")
    private String rulesSource = null;

    @SerializedName("permissions")
    private Map<String, Map<String, Boolean>> permissions = null;

    @SerializedName("ownerId")
    private String ownerId = null;

    /**
     * Note: May be 0 on endpoints returning lists of Networks
     **/
    @SerializedName("onlineMemberCount")
    private Integer onlineMemberCount = null;

    @SerializedName("authorizedMemberCount")
    private Integer authorizedMemberCount = null;

    @SerializedName("totalMemberCount")
    private Integer totalMemberCount = null;

    @SerializedName("capabilitiesByName")
    private Object capabilitiesByName = null;

    @SerializedName("tagsByName")
    private Object tagsByName = null;

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

    public NetworkConfig getConfig() {
        return config;
    }

    public void setConfig(NetworkConfig config) {
        this.config = config;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRulesSource() {
        return rulesSource;
    }

    public void setRulesSource(String rulesSource) {
        this.rulesSource = rulesSource;
    }

    public Map<String, Map<String, Boolean>> getPermissions() {
        return permissions;
    }

    public void setPermissions(Map<String, Map<String, Boolean>> permissions) {
        this.permissions = permissions;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Integer getOnlineMemberCount() {
        return onlineMemberCount;
    }

    public void setOnlineMemberCount(Integer onlineMemberCount) {
        this.onlineMemberCount = onlineMemberCount;
    }

    public Integer getAuthorizedMemberCount() {
        return authorizedMemberCount;
    }

    public void setAuthorizedMemberCount(Integer authorizedMemberCount) {
        this.authorizedMemberCount = authorizedMemberCount;
    }

    public Integer getTotalMemberCount() {
        return totalMemberCount;
    }

    public void setTotalMemberCount(Integer totalMemberCount) {
        this.totalMemberCount = totalMemberCount;
    }

    public Object getCapabilitiesByName() {
        return capabilitiesByName;
    }

    public void setCapabilitiesByName(Object capabilitiesByName) {
        this.capabilitiesByName = capabilitiesByName;
    }

    public Object getTagsByName() {
        return tagsByName;
    }

    public void setTagsByName(Object tagsByName) {
        this.tagsByName = tagsByName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Network network = (Network) o;
        return Objects.equals(id, network.id) && Objects.equals(clock, network.clock) && Objects.equals(config, network.config) && Objects.equals(description, network.description) && Objects.equals(rulesSource, network.rulesSource) && Objects.equals(permissions, network.permissions) && Objects.equals(ownerId, network.ownerId) && Objects.equals(onlineMemberCount, network.onlineMemberCount) && Objects.equals(authorizedMemberCount, network.authorizedMemberCount) && Objects.equals(totalMemberCount, network.totalMemberCount) && Objects.equals(capabilitiesByName, network.capabilitiesByName) && Objects.equals(tagsByName, network.tagsByName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, clock, config, description, rulesSource, permissions, ownerId, onlineMemberCount, authorizedMemberCount, totalMemberCount, capabilitiesByName, tagsByName);
    }

    @Override
    public String toString() {
        return "Network{" +
                "id='" + id + '\'' +
                ", clock=" + clock +
                ", config=" + config +
                ", description='" + description + '\'' +
                ", rulesSource='" + rulesSource + '\'' +
                ", permissions=" + permissions +
                ", ownerId='" + ownerId + '\'' +
                ", onlineMemberCount=" + onlineMemberCount +
                ", authorizedMemberCount=" + authorizedMemberCount +
                ", totalMemberCount=" + totalMemberCount +
                ", capabilitiesByName=" + capabilitiesByName +
                ", tagsByName=" + tagsByName +
                '}';
    }
}
