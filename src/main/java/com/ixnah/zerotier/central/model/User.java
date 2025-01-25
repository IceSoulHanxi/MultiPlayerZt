package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User   {

    /**
     * User ID
     **/
    @SerializedName("id")
    private String id = null;

    /**
     * Organization ID
     **/
    @SerializedName("orgId")
    private String orgId = null;

    @SerializedName("globalPermissions")
    private Map<String, Boolean> globalPermissions = null;

    /**
     * Display Name
     **/
    @SerializedName("displayName")
    private String displayName = null;

    /**
     * User email address
     **/
    @SerializedName("email")
    private String email = null;

    @SerializedName("auth")
    private Map<String, String> auth = null;

    /**
     * SMS number
     **/
    @SerializedName("smsNumber")
    private String smsNumber = null;

    /**
     * List of API token names.
     **/
    @SerializedName("tokens")
    private List<String> tokens = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Map<String, Boolean> getGlobalPermissions() {
        return globalPermissions;
    }

    public void setGlobalPermissions(Map<String, Boolean> globalPermissions) {
        this.globalPermissions = globalPermissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Map<String, String> getAuth() {
        return auth;
    }

    public void setAuth(Map<String, String> auth) {
        this.auth = auth;
    }

    public String getSmsNumber() {
        return smsNumber;
    }

    public void setSmsNumber(String smsNumber) {
        this.smsNumber = smsNumber;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(orgId, user.orgId) && Objects.equals(globalPermissions, user.globalPermissions) && Objects.equals(displayName, user.displayName) && Objects.equals(email, user.email) && Objects.equals(auth, user.auth) && Objects.equals(smsNumber, user.smsNumber) && Objects.equals(tokens, user.tokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orgId, globalPermissions, displayName, email, auth, smsNumber, tokens);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", orgId='" + orgId + '\'' +
                ", globalPermissions=" + globalPermissions +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", auth=" + auth +
                ", smsNumber='" + smsNumber + '\'' +
                ", tokens=" + tokens +
                '}';
    }
}
