package com.ixnah.zerotier.central.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OidcConfiguration {

    @SerializedName("issuer")
    private String issuer;
    @SerializedName("authorization_endpoint")
    private String authorizationEndpoint;
    @SerializedName("token_endpoint")
    private String tokenEndpoint;
    @SerializedName("introspection_endpoint")
    private String introspectionEndpoint;
    @SerializedName("userinfo_endpoint")
    private String userinfoEndpoint;
    @SerializedName("end_session_endpoint")
    private String endSessionEndpoint;
    @SerializedName("frontchannel_logout_session_supported")
    private boolean frontChannelLogoutSessionSupported;
    @SerializedName("frontchannel_logout_supported")
    private boolean frontChannelLogoutSupported;
    @SerializedName("jwks_uri")
    private String jwksUri;
    @SerializedName("check_session_iframe")
    private String checkSessionIframe;
    @SerializedName("grant_types_supported")
    private List<String> grantTypesSupported;
    @SerializedName("acr_values_supported")
    private List<String> acrValuesSupported;
    @SerializedName("response_types_supported")
    private List<String> responseTypesSupported;
    @SerializedName("subject_types_supported")
    private List<String> subjectTypesSupported;
    @SerializedName("id_token_signing_alg_values_supported")
    private List<String> idTokenSigningAlgValuesSupported;
    @SerializedName("id_token_encryption_alg_values_supported")
    private List<String> idTokenEncryptionAlgValuesSupported;
    @SerializedName("id_token_encryption_enc_values_supported")
    private List<String> idTokenEncryptionEncValuesSupported;
    @SerializedName("userinfo_signing_alg_values_supported")
    private List<String> userinfoSigningAlgValuesSupported;
    @SerializedName("userinfo_encryption_alg_values_supported")
    private List<String> userinfoEncryptionAlgValuesSupported;
    @SerializedName("userinfo_encryption_enc_values_supported")
    private List<String> userinfoEncryptionEncValuesSupported;
    @SerializedName("request_object_signing_alg_values_supported")
    private List<String> requestObjectSigningAlgValuesSupported;
    @SerializedName("request_object_encryption_alg_values_supported")
    private List<String> requestObjectEncryptionAlgValuesSupported;
    @SerializedName("request_object_encryption_enc_values_supported")
    private List<String> requestObjectEncryptionEncValuesSupported;
    @SerializedName("response_modes_supported")
    private List<String> responseModesSupported;
    @SerializedName("registration_endpoint")
    private String registrationEndpoint;
    @SerializedName("token_endpoint_auth_methods_supported")
    private List<String> tokenEndpointAuthMethodsSupported;
    @SerializedName("token_endpoint_auth_signing_alg_values_supported")
    private List<String> tokenEndpointAuthSigningAlgValuesSupported;
    @SerializedName("introspection_endpoint_auth_methods_supported")
    private List<String> introspectionEndpointAuthMethodsSupported;
    @SerializedName("introspection_endpoint_auth_signing_alg_values_supported")
    private List<String> introspectionEndpointAuthSigningAlgValuesSupported;
    @SerializedName("authorization_signing_alg_values_supported")
    private List<String> authorizationSigningAlgValuesSupported;
    @SerializedName("authorization_encryption_alg_values_supported")
    private List<String> authorizationEncryptionAlgValuesSupported;
    @SerializedName("authorization_encryption_enc_values_supported")
    private List<String> authorizationEncryptionEncValuesSupported;
    @SerializedName("claims_supported")
    private List<String> claimsSupported;
    @SerializedName("claim_types_supported")
    private List<String> claimTypesSupported;
    @SerializedName("claims_parameter_supported")
    private boolean claimsParameterSupported;
    @SerializedName("scopes_supported")
    private List<String> scopesSupported;
    @SerializedName("request_parameter_supported")
    private boolean requestParameterSupported;
    @SerializedName("request_uri_parameter_supported")
    private boolean requestUriParameterSupported;
    @SerializedName("require_request_uri_registration")
    private boolean requireRequestUriRegistration;
    @SerializedName("code_challenge_methods_supported")
    private List<String> codeChallengeMethodsSupported;
    @SerializedName("tls_client_certificate_bound_access_tokens")
    private boolean tlsClientCertificateBoundAccessTokens;
    @SerializedName("revocation_endpoint")
    private String revocationEndpoint;
    @SerializedName("revocation_endpoint_auth_methods_supported")
    private List<String> revocationEndpointAuthMethodsSupported;
    @SerializedName("revocation_endpoint_auth_signing_alg_values_supported")
    private List<String> revocationEndpointAuthSigningAlgValuesSupported;
    @SerializedName("backchannel_logout_supported")
    private boolean backChannelLogoutSupported;
    @SerializedName("backchannel_logout_session_supported")
    private boolean backChannelLogoutSessionSupported;
    @SerializedName("device_authorization_endpoint")
    private String deviceAuthorizationEndpoint;
    @SerializedName("backchannel_token_delivery_modes_supported")
    private List<String> backChannelTokenDeliveryModesSupported;
    @SerializedName("backchannel_authentication_endpoint")
    private String backChannelAuthenticationEndpoint;
    @SerializedName("backchannel_authentication_request_signing_alg_values_supported")
    private List<String> backChannelAuthenticationRequestSigningAlgValuesSupported;
    @SerializedName("require_pushed_authorization_requests")
    private boolean requirePushedAuthorizationRequests;
    @SerializedName("pushed_authorization_request_endpoint")
    private String pushedAuthorizationRequestEndpoint;
    @SerializedName("mtls_endpoint_aliases")
    private Map<String, String> mtlsEndpointAliases;
    @SerializedName("authorization_response_iss_parameter_supported")
    private boolean authorizationResponseIssParameterSupported;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getIntrospectionEndpoint() {
        return introspectionEndpoint;
    }

    public void setIntrospectionEndpoint(String introspectionEndpoint) {
        this.introspectionEndpoint = introspectionEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public String getEndSessionEndpoint() {
        return endSessionEndpoint;
    }

    public void setEndSessionEndpoint(String endSessionEndpoint) {
        this.endSessionEndpoint = endSessionEndpoint;
    }

    public boolean isFrontChannelLogoutSessionSupported() {
        return frontChannelLogoutSessionSupported;
    }

    public void setFrontChannelLogoutSessionSupported(boolean frontChannelLogoutSessionSupported) {
        this.frontChannelLogoutSessionSupported = frontChannelLogoutSessionSupported;
    }

    public boolean isFrontChannelLogoutSupported() {
        return frontChannelLogoutSupported;
    }

    public void setFrontChannelLogoutSupported(boolean frontChannelLogoutSupported) {
        this.frontChannelLogoutSupported = frontChannelLogoutSupported;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public String getCheckSessionIframe() {
        return checkSessionIframe;
    }

    public void setCheckSessionIframe(String checkSessionIframe) {
        this.checkSessionIframe = checkSessionIframe;
    }

    public List<String> getGrantTypesSupported() {
        return grantTypesSupported;
    }

    public void setGrantTypesSupported(List<String> grantTypesSupported) {
        this.grantTypesSupported = grantTypesSupported;
    }

    public List<String> getAcrValuesSupported() {
        return acrValuesSupported;
    }

    public void setAcrValuesSupported(List<String> acrValuesSupported) {
        this.acrValuesSupported = acrValuesSupported;
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public List<String> getIdTokenSigningAlgValuesSupported() {
        return idTokenSigningAlgValuesSupported;
    }

    public void setIdTokenSigningAlgValuesSupported(List<String> idTokenSigningAlgValuesSupported) {
        this.idTokenSigningAlgValuesSupported = idTokenSigningAlgValuesSupported;
    }

    public List<String> getIdTokenEncryptionAlgValuesSupported() {
        return idTokenEncryptionAlgValuesSupported;
    }

    public void setIdTokenEncryptionAlgValuesSupported(List<String> idTokenEncryptionAlgValuesSupported) {
        this.idTokenEncryptionAlgValuesSupported = idTokenEncryptionAlgValuesSupported;
    }

    public List<String> getIdTokenEncryptionEncValuesSupported() {
        return idTokenEncryptionEncValuesSupported;
    }

    public void setIdTokenEncryptionEncValuesSupported(List<String> idTokenEncryptionEncValuesSupported) {
        this.idTokenEncryptionEncValuesSupported = idTokenEncryptionEncValuesSupported;
    }

    public List<String> getUserinfoSigningAlgValuesSupported() {
        return userinfoSigningAlgValuesSupported;
    }

    public void setUserinfoSigningAlgValuesSupported(List<String> userinfoSigningAlgValuesSupported) {
        this.userinfoSigningAlgValuesSupported = userinfoSigningAlgValuesSupported;
    }

    public List<String> getUserinfoEncryptionAlgValuesSupported() {
        return userinfoEncryptionAlgValuesSupported;
    }

    public void setUserinfoEncryptionAlgValuesSupported(List<String> userinfoEncryptionAlgValuesSupported) {
        this.userinfoEncryptionAlgValuesSupported = userinfoEncryptionAlgValuesSupported;
    }

    public List<String> getUserinfoEncryptionEncValuesSupported() {
        return userinfoEncryptionEncValuesSupported;
    }

    public void setUserinfoEncryptionEncValuesSupported(List<String> userinfoEncryptionEncValuesSupported) {
        this.userinfoEncryptionEncValuesSupported = userinfoEncryptionEncValuesSupported;
    }

    public List<String> getRequestObjectSigningAlgValuesSupported() {
        return requestObjectSigningAlgValuesSupported;
    }

    public void setRequestObjectSigningAlgValuesSupported(List<String> requestObjectSigningAlgValuesSupported) {
        this.requestObjectSigningAlgValuesSupported = requestObjectSigningAlgValuesSupported;
    }

    public List<String> getRequestObjectEncryptionAlgValuesSupported() {
        return requestObjectEncryptionAlgValuesSupported;
    }

    public void setRequestObjectEncryptionAlgValuesSupported(List<String> requestObjectEncryptionAlgValuesSupported) {
        this.requestObjectEncryptionAlgValuesSupported = requestObjectEncryptionAlgValuesSupported;
    }

    public List<String> getRequestObjectEncryptionEncValuesSupported() {
        return requestObjectEncryptionEncValuesSupported;
    }

    public void setRequestObjectEncryptionEncValuesSupported(List<String> requestObjectEncryptionEncValuesSupported) {
        this.requestObjectEncryptionEncValuesSupported = requestObjectEncryptionEncValuesSupported;
    }

    public List<String> getResponseModesSupported() {
        return responseModesSupported;
    }

    public void setResponseModesSupported(List<String> responseModesSupported) {
        this.responseModesSupported = responseModesSupported;
    }

    public String getRegistrationEndpoint() {
        return registrationEndpoint;
    }

    public void setRegistrationEndpoint(String registrationEndpoint) {
        this.registrationEndpoint = registrationEndpoint;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public void setTokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    public List<String> getTokenEndpointAuthSigningAlgValuesSupported() {
        return tokenEndpointAuthSigningAlgValuesSupported;
    }

    public void setTokenEndpointAuthSigningAlgValuesSupported(List<String> tokenEndpointAuthSigningAlgValuesSupported) {
        this.tokenEndpointAuthSigningAlgValuesSupported = tokenEndpointAuthSigningAlgValuesSupported;
    }

    public List<String> getIntrospectionEndpointAuthMethodsSupported() {
        return introspectionEndpointAuthMethodsSupported;
    }

    public void setIntrospectionEndpointAuthMethodsSupported(List<String> introspectionEndpointAuthMethodsSupported) {
        this.introspectionEndpointAuthMethodsSupported = introspectionEndpointAuthMethodsSupported;
    }

    public List<String> getIntrospectionEndpointAuthSigningAlgValuesSupported() {
        return introspectionEndpointAuthSigningAlgValuesSupported;
    }

    public void setIntrospectionEndpointAuthSigningAlgValuesSupported(List<String> introspectionEndpointAuthSigningAlgValuesSupported) {
        this.introspectionEndpointAuthSigningAlgValuesSupported = introspectionEndpointAuthSigningAlgValuesSupported;
    }

    public List<String> getAuthorizationSigningAlgValuesSupported() {
        return authorizationSigningAlgValuesSupported;
    }

    public void setAuthorizationSigningAlgValuesSupported(List<String> authorizationSigningAlgValuesSupported) {
        this.authorizationSigningAlgValuesSupported = authorizationSigningAlgValuesSupported;
    }

    public List<String> getAuthorizationEncryptionAlgValuesSupported() {
        return authorizationEncryptionAlgValuesSupported;
    }

    public void setAuthorizationEncryptionAlgValuesSupported(List<String> authorizationEncryptionAlgValuesSupported) {
        this.authorizationEncryptionAlgValuesSupported = authorizationEncryptionAlgValuesSupported;
    }

    public List<String> getAuthorizationEncryptionEncValuesSupported() {
        return authorizationEncryptionEncValuesSupported;
    }

    public void setAuthorizationEncryptionEncValuesSupported(List<String> authorizationEncryptionEncValuesSupported) {
        this.authorizationEncryptionEncValuesSupported = authorizationEncryptionEncValuesSupported;
    }

    public List<String> getClaimsSupported() {
        return claimsSupported;
    }

    public void setClaimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
    }

    public List<String> getClaimTypesSupported() {
        return claimTypesSupported;
    }

    public void setClaimTypesSupported(List<String> claimTypesSupported) {
        this.claimTypesSupported = claimTypesSupported;
    }

    public boolean isClaimsParameterSupported() {
        return claimsParameterSupported;
    }

    public void setClaimsParameterSupported(boolean claimsParameterSupported) {
        this.claimsParameterSupported = claimsParameterSupported;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public void setScopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    public boolean isRequestParameterSupported() {
        return requestParameterSupported;
    }

    public void setRequestParameterSupported(boolean requestParameterSupported) {
        this.requestParameterSupported = requestParameterSupported;
    }

    public boolean isRequestUriParameterSupported() {
        return requestUriParameterSupported;
    }

    public void setRequestUriParameterSupported(boolean requestUriParameterSupported) {
        this.requestUriParameterSupported = requestUriParameterSupported;
    }

    public boolean isRequireRequestUriRegistration() {
        return requireRequestUriRegistration;
    }

    public void setRequireRequestUriRegistration(boolean requireRequestUriRegistration) {
        this.requireRequestUriRegistration = requireRequestUriRegistration;
    }

    public List<String> getCodeChallengeMethodsSupported() {
        return codeChallengeMethodsSupported;
    }

    public void setCodeChallengeMethodsSupported(List<String> codeChallengeMethodsSupported) {
        this.codeChallengeMethodsSupported = codeChallengeMethodsSupported;
    }

    public boolean isTlsClientCertificateBoundAccessTokens() {
        return tlsClientCertificateBoundAccessTokens;
    }

    public void setTlsClientCertificateBoundAccessTokens(boolean tlsClientCertificateBoundAccessTokens) {
        this.tlsClientCertificateBoundAccessTokens = tlsClientCertificateBoundAccessTokens;
    }

    public String getRevocationEndpoint() {
        return revocationEndpoint;
    }

    public void setRevocationEndpoint(String revocationEndpoint) {
        this.revocationEndpoint = revocationEndpoint;
    }

    public List<String> getRevocationEndpointAuthMethodsSupported() {
        return revocationEndpointAuthMethodsSupported;
    }

    public void setRevocationEndpointAuthMethodsSupported(List<String> revocationEndpointAuthMethodsSupported) {
        this.revocationEndpointAuthMethodsSupported = revocationEndpointAuthMethodsSupported;
    }

    public List<String> getRevocationEndpointAuthSigningAlgValuesSupported() {
        return revocationEndpointAuthSigningAlgValuesSupported;
    }

    public void setRevocationEndpointAuthSigningAlgValuesSupported(List<String> revocationEndpointAuthSigningAlgValuesSupported) {
        this.revocationEndpointAuthSigningAlgValuesSupported = revocationEndpointAuthSigningAlgValuesSupported;
    }

    public boolean isBackChannelLogoutSupported() {
        return backChannelLogoutSupported;
    }

    public void setBackChannelLogoutSupported(boolean backChannelLogoutSupported) {
        this.backChannelLogoutSupported = backChannelLogoutSupported;
    }

    public boolean isBackChannelLogoutSessionSupported() {
        return backChannelLogoutSessionSupported;
    }

    public void setBackChannelLogoutSessionSupported(boolean backChannelLogoutSessionSupported) {
        this.backChannelLogoutSessionSupported = backChannelLogoutSessionSupported;
    }

    public String getDeviceAuthorizationEndpoint() {
        return deviceAuthorizationEndpoint;
    }

    public void setDeviceAuthorizationEndpoint(String deviceAuthorizationEndpoint) {
        this.deviceAuthorizationEndpoint = deviceAuthorizationEndpoint;
    }

    public List<String> getBackChannelTokenDeliveryModesSupported() {
        return backChannelTokenDeliveryModesSupported;
    }

    public void setBackChannelTokenDeliveryModesSupported(List<String> backChannelTokenDeliveryModesSupported) {
        this.backChannelTokenDeliveryModesSupported = backChannelTokenDeliveryModesSupported;
    }

    public String getBackChannelAuthenticationEndpoint() {
        return backChannelAuthenticationEndpoint;
    }

    public void setBackChannelAuthenticationEndpoint(String backChannelAuthenticationEndpoint) {
        this.backChannelAuthenticationEndpoint = backChannelAuthenticationEndpoint;
    }

    public List<String> getBackChannelAuthenticationRequestSigningAlgValuesSupported() {
        return backChannelAuthenticationRequestSigningAlgValuesSupported;
    }

    public void setBackChannelAuthenticationRequestSigningAlgValuesSupported(List<String> backChannelAuthenticationRequestSigningAlgValuesSupported) {
        this.backChannelAuthenticationRequestSigningAlgValuesSupported = backChannelAuthenticationRequestSigningAlgValuesSupported;
    }

    public boolean isRequirePushedAuthorizationRequests() {
        return requirePushedAuthorizationRequests;
    }

    public void setRequirePushedAuthorizationRequests(boolean requirePushedAuthorizationRequests) {
        this.requirePushedAuthorizationRequests = requirePushedAuthorizationRequests;
    }

    public String getPushedAuthorizationRequestEndpoint() {
        return pushedAuthorizationRequestEndpoint;
    }

    public void setPushedAuthorizationRequestEndpoint(String pushedAuthorizationRequestEndpoint) {
        this.pushedAuthorizationRequestEndpoint = pushedAuthorizationRequestEndpoint;
    }

    public Map<String, String> getMtlsEndpointAliases() {
        return mtlsEndpointAliases;
    }

    public void setMtlsEndpointAliases(Map<String, String> mtlsEndpointAliases) {
        this.mtlsEndpointAliases = mtlsEndpointAliases;
    }

    public boolean isAuthorizationResponseIssParameterSupported() {
        return authorizationResponseIssParameterSupported;
    }

    public void setAuthorizationResponseIssParameterSupported(boolean authorizationResponseIssParameterSupported) {
        this.authorizationResponseIssParameterSupported = authorizationResponseIssParameterSupported;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OidcConfiguration that = (OidcConfiguration) o;
        return frontChannelLogoutSessionSupported == that.frontChannelLogoutSessionSupported && frontChannelLogoutSupported == that.frontChannelLogoutSupported && claimsParameterSupported == that.claimsParameterSupported && requestParameterSupported == that.requestParameterSupported && requestUriParameterSupported == that.requestUriParameterSupported && requireRequestUriRegistration == that.requireRequestUriRegistration && tlsClientCertificateBoundAccessTokens == that.tlsClientCertificateBoundAccessTokens && backChannelLogoutSupported == that.backChannelLogoutSupported && backChannelLogoutSessionSupported == that.backChannelLogoutSessionSupported && requirePushedAuthorizationRequests == that.requirePushedAuthorizationRequests && authorizationResponseIssParameterSupported == that.authorizationResponseIssParameterSupported && Objects.equals(issuer, that.issuer) && Objects.equals(authorizationEndpoint, that.authorizationEndpoint) && Objects.equals(tokenEndpoint, that.tokenEndpoint) && Objects.equals(introspectionEndpoint, that.introspectionEndpoint) && Objects.equals(userinfoEndpoint, that.userinfoEndpoint) && Objects.equals(endSessionEndpoint, that.endSessionEndpoint) && Objects.equals(jwksUri, that.jwksUri) && Objects.equals(checkSessionIframe, that.checkSessionIframe) && Objects.equals(grantTypesSupported, that.grantTypesSupported) && Objects.equals(acrValuesSupported, that.acrValuesSupported) && Objects.equals(responseTypesSupported, that.responseTypesSupported) && Objects.equals(subjectTypesSupported, that.subjectTypesSupported) && Objects.equals(idTokenSigningAlgValuesSupported, that.idTokenSigningAlgValuesSupported) && Objects.equals(idTokenEncryptionAlgValuesSupported, that.idTokenEncryptionAlgValuesSupported) && Objects.equals(idTokenEncryptionEncValuesSupported, that.idTokenEncryptionEncValuesSupported) && Objects.equals(userinfoSigningAlgValuesSupported, that.userinfoSigningAlgValuesSupported) && Objects.equals(userinfoEncryptionAlgValuesSupported, that.userinfoEncryptionAlgValuesSupported) && Objects.equals(userinfoEncryptionEncValuesSupported, that.userinfoEncryptionEncValuesSupported) && Objects.equals(requestObjectSigningAlgValuesSupported, that.requestObjectSigningAlgValuesSupported) && Objects.equals(requestObjectEncryptionAlgValuesSupported, that.requestObjectEncryptionAlgValuesSupported) && Objects.equals(requestObjectEncryptionEncValuesSupported, that.requestObjectEncryptionEncValuesSupported) && Objects.equals(responseModesSupported, that.responseModesSupported) && Objects.equals(registrationEndpoint, that.registrationEndpoint) && Objects.equals(tokenEndpointAuthMethodsSupported, that.tokenEndpointAuthMethodsSupported) && Objects.equals(tokenEndpointAuthSigningAlgValuesSupported, that.tokenEndpointAuthSigningAlgValuesSupported) && Objects.equals(introspectionEndpointAuthMethodsSupported, that.introspectionEndpointAuthMethodsSupported) && Objects.equals(introspectionEndpointAuthSigningAlgValuesSupported, that.introspectionEndpointAuthSigningAlgValuesSupported) && Objects.equals(authorizationSigningAlgValuesSupported, that.authorizationSigningAlgValuesSupported) && Objects.equals(authorizationEncryptionAlgValuesSupported, that.authorizationEncryptionAlgValuesSupported) && Objects.equals(authorizationEncryptionEncValuesSupported, that.authorizationEncryptionEncValuesSupported) && Objects.equals(claimsSupported, that.claimsSupported) && Objects.equals(claimTypesSupported, that.claimTypesSupported) && Objects.equals(scopesSupported, that.scopesSupported) && Objects.equals(codeChallengeMethodsSupported, that.codeChallengeMethodsSupported) && Objects.equals(revocationEndpoint, that.revocationEndpoint) && Objects.equals(revocationEndpointAuthMethodsSupported, that.revocationEndpointAuthMethodsSupported) && Objects.equals(revocationEndpointAuthSigningAlgValuesSupported, that.revocationEndpointAuthSigningAlgValuesSupported) && Objects.equals(deviceAuthorizationEndpoint, that.deviceAuthorizationEndpoint) && Objects.equals(backChannelTokenDeliveryModesSupported, that.backChannelTokenDeliveryModesSupported) && Objects.equals(backChannelAuthenticationEndpoint, that.backChannelAuthenticationEndpoint) && Objects.equals(backChannelAuthenticationRequestSigningAlgValuesSupported, that.backChannelAuthenticationRequestSigningAlgValuesSupported) && Objects.equals(pushedAuthorizationRequestEndpoint, that.pushedAuthorizationRequestEndpoint) && Objects.equals(mtlsEndpointAliases, that.mtlsEndpointAliases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuer, authorizationEndpoint, tokenEndpoint, introspectionEndpoint, userinfoEndpoint, endSessionEndpoint, frontChannelLogoutSessionSupported, frontChannelLogoutSupported, jwksUri, checkSessionIframe, grantTypesSupported, acrValuesSupported, responseTypesSupported, subjectTypesSupported, idTokenSigningAlgValuesSupported, idTokenEncryptionAlgValuesSupported, idTokenEncryptionEncValuesSupported, userinfoSigningAlgValuesSupported, userinfoEncryptionAlgValuesSupported, userinfoEncryptionEncValuesSupported, requestObjectSigningAlgValuesSupported, requestObjectEncryptionAlgValuesSupported, requestObjectEncryptionEncValuesSupported, responseModesSupported, registrationEndpoint, tokenEndpointAuthMethodsSupported, tokenEndpointAuthSigningAlgValuesSupported, introspectionEndpointAuthMethodsSupported, introspectionEndpointAuthSigningAlgValuesSupported, authorizationSigningAlgValuesSupported, authorizationEncryptionAlgValuesSupported, authorizationEncryptionEncValuesSupported, claimsSupported, claimTypesSupported, claimsParameterSupported, scopesSupported, requestParameterSupported, requestUriParameterSupported, requireRequestUriRegistration, codeChallengeMethodsSupported, tlsClientCertificateBoundAccessTokens, revocationEndpoint, revocationEndpointAuthMethodsSupported, revocationEndpointAuthSigningAlgValuesSupported, backChannelLogoutSupported, backChannelLogoutSessionSupported, deviceAuthorizationEndpoint, backChannelTokenDeliveryModesSupported, backChannelAuthenticationEndpoint, backChannelAuthenticationRequestSigningAlgValuesSupported, requirePushedAuthorizationRequests, pushedAuthorizationRequestEndpoint, mtlsEndpointAliases, authorizationResponseIssParameterSupported);
    }

    @Override
    public String toString() {
        return "OidcConfiguration{" +
                "issuer='" + issuer + '\'' +
                ", authorizationEndpoint='" + authorizationEndpoint + '\'' +
                ", tokenEndpoint='" + tokenEndpoint + '\'' +
                ", introspectionEndpoint='" + introspectionEndpoint + '\'' +
                ", userinfoEndpoint='" + userinfoEndpoint + '\'' +
                ", endSessionEndpoint='" + endSessionEndpoint + '\'' +
                ", frontChannelLogoutSessionSupported=" + frontChannelLogoutSessionSupported +
                ", frontChannelLogoutSupported=" + frontChannelLogoutSupported +
                ", jwksUri='" + jwksUri + '\'' +
                ", checkSessionIframe='" + checkSessionIframe + '\'' +
                ", grantTypesSupported=" + grantTypesSupported +
                ", acrValuesSupported=" + acrValuesSupported +
                ", responseTypesSupported=" + responseTypesSupported +
                ", subjectTypesSupported=" + subjectTypesSupported +
                ", idTokenSigningAlgValuesSupported=" + idTokenSigningAlgValuesSupported +
                ", idTokenEncryptionAlgValuesSupported=" + idTokenEncryptionAlgValuesSupported +
                ", idTokenEncryptionEncValuesSupported=" + idTokenEncryptionEncValuesSupported +
                ", userinfoSigningAlgValuesSupported=" + userinfoSigningAlgValuesSupported +
                ", userinfoEncryptionAlgValuesSupported=" + userinfoEncryptionAlgValuesSupported +
                ", userinfoEncryptionEncValuesSupported=" + userinfoEncryptionEncValuesSupported +
                ", requestObjectSigningAlgValuesSupported=" + requestObjectSigningAlgValuesSupported +
                ", requestObjectEncryptionAlgValuesSupported=" + requestObjectEncryptionAlgValuesSupported +
                ", requestObjectEncryptionEncValuesSupported=" + requestObjectEncryptionEncValuesSupported +
                ", responseModesSupported=" + responseModesSupported +
                ", registrationEndpoint='" + registrationEndpoint + '\'' +
                ", tokenEndpointAuthMethodsSupported=" + tokenEndpointAuthMethodsSupported +
                ", tokenEndpointAuthSigningAlgValuesSupported=" + tokenEndpointAuthSigningAlgValuesSupported +
                ", introspectionEndpointAuthMethodsSupported=" + introspectionEndpointAuthMethodsSupported +
                ", introspectionEndpointAuthSigningAlgValuesSupported=" + introspectionEndpointAuthSigningAlgValuesSupported +
                ", authorizationSigningAlgValuesSupported=" + authorizationSigningAlgValuesSupported +
                ", authorizationEncryptionAlgValuesSupported=" + authorizationEncryptionAlgValuesSupported +
                ", authorizationEncryptionEncValuesSupported=" + authorizationEncryptionEncValuesSupported +
                ", claimsSupported=" + claimsSupported +
                ", claimTypesSupported=" + claimTypesSupported +
                ", claimsParameterSupported=" + claimsParameterSupported +
                ", scopesSupported=" + scopesSupported +
                ", requestParameterSupported=" + requestParameterSupported +
                ", requestUriParameterSupported=" + requestUriParameterSupported +
                ", requireRequestUriRegistration=" + requireRequestUriRegistration +
                ", codeChallengeMethodsSupported=" + codeChallengeMethodsSupported +
                ", tlsClientCertificateBoundAccessTokens=" + tlsClientCertificateBoundAccessTokens +
                ", revocationEndpoint='" + revocationEndpoint + '\'' +
                ", revocationEndpointAuthMethodsSupported=" + revocationEndpointAuthMethodsSupported +
                ", revocationEndpointAuthSigningAlgValuesSupported=" + revocationEndpointAuthSigningAlgValuesSupported +
                ", backChannelLogoutSupported=" + backChannelLogoutSupported +
                ", backChannelLogoutSessionSupported=" + backChannelLogoutSessionSupported +
                ", deviceAuthorizationEndpoint='" + deviceAuthorizationEndpoint + '\'' +
                ", backChannelTokenDeliveryModesSupported=" + backChannelTokenDeliveryModesSupported +
                ", backChannelAuthenticationEndpoint='" + backChannelAuthenticationEndpoint + '\'' +
                ", backChannelAuthenticationRequestSigningAlgValuesSupported=" + backChannelAuthenticationRequestSigningAlgValuesSupported +
                ", requirePushedAuthorizationRequests=" + requirePushedAuthorizationRequests +
                ", pushedAuthorizationRequestEndpoint='" + pushedAuthorizationRequestEndpoint + '\'' +
                ", mtlsEndpointAliases=" + mtlsEndpointAliases +
                ", authorizationResponseIssParameterSupported=" + authorizationResponseIssParameterSupported +
                '}';
    }
}