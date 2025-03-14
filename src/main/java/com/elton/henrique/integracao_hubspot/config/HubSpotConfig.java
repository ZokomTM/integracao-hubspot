package com.elton.henrique.integracao_hubspot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HubSpotConfig {
    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.scopes}")
    private String scopes;

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public String getScopes() {
        return scopes;
    }

    private static final String HUBSPOT_AUTH_URL = "https://app.hubspot.com/oauth/authorize";

    public String getHubSpotAuthUrl() {
        return String.format("%s?client_id=%s&redirect_uri=%s&scope=%s&response_type=code",
                HUBSPOT_AUTH_URL, clientId, redirectUri, scopes.replace(" ", "%20"));
    }
}
