package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;
import java.util.Objects;

public class Status {

    @SerializedName("id")
    private String id = null;

    @SerializedName("type")
    private String type = null;

    /**
     * Current time on server
     **/
    @SerializedName("clock")
    private Long clock = null;

    @SerializedName("version")
    private String version = null;

    @SerializedName("apiVersion")
    private String apiVersion = null;

    /**
     * Uptime on server
     **/
    @SerializedName("uptime")
    private Long uptime = null;

    @SerializedName("user")
    private User user = null;

    @SerializedName("readOnlyMode")
    private Boolean readOnlyMode = null;

    @SerializedName("loginMethods")
    private Map<String, Boolean> loginMethods = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getClock() {
        return clock;
    }

    public void setClock(Long clock) {
        this.clock = clock;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Long getUptime() {
        return uptime;
    }

    public void setUptime(Long uptime) {
        this.uptime = uptime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Boolean getReadOnlyMode() {
        return readOnlyMode;
    }

    public void setReadOnlyMode(Boolean readOnlyMode) {
        this.readOnlyMode = readOnlyMode;
    }

    public Map<String, Boolean> getLoginMethods() {
        return loginMethods;
    }

    public void setLoginMethods(Map<String, Boolean> loginMethods) {
        this.loginMethods = loginMethods;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(id, status.id) && Objects.equals(type, status.type) && Objects.equals(clock, status.clock) && Objects.equals(version, status.version) && Objects.equals(apiVersion, status.apiVersion) && Objects.equals(uptime, status.uptime) && Objects.equals(user, status.user) && Objects.equals(readOnlyMode, status.readOnlyMode) && Objects.equals(loginMethods, status.loginMethods);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, clock, version, apiVersion, uptime, user, readOnlyMode, loginMethods);
    }

    @Override
    public String toString() {
        return "Status{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", clock=" + clock +
                ", version='" + version + '\'' +
                ", apiVersion='" + apiVersion + '\'' +
                ", uptime=" + uptime +
                ", user=" + user +
                ", readOnlyMode=" + readOnlyMode +
                ", loginMethods=" + loginMethods +
                '}';
    }
}