package com.elton.henrique.integracao_hubspot.services;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.responses.TokenResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    @Autowired
    private HubSpotConfig hubSpotConfig;

    @Autowired
    private TokenService tokenService;

    public String getAccessToken(String code) throws Exception {
        String clientId = hubSpotConfig.getClientId();
        String clientSecret = hubSpotConfig.getClientSecret();
        String redirectUri = hubSpotConfig.getRedirectUri();
        String tokenUrl = hubSpotConfig.getHubspotUrlToken();

        String body = "grant_type=authorization_code" +
                "&client_id=" + clientId +
                "&client_secret=" + clientSecret +
                "&redirect_uri=" + redirectUri +
                "&code=" + code;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        TokenResponse tokenResponse = objectMapper.readValue(response.getBody(), TokenResponse.class);

        tokenService.saveToken(
                tokenResponse.getTokenType(),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                tokenResponse.getExpiresIn()
        );

        return tokenResponse.getAccessToken();
    }
}
