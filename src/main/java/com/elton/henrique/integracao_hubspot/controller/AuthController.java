package com.elton.henrique.integracao_hubspot.controller;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.responses.TokenResponse;
import com.elton.henrique.integracao_hubspot.services.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/oauth")
public class AuthController {

    private final HubSpotConfig hubSpotConfig;
    @Autowired
    private TokenService tokenService;

    public AuthController(HubSpotConfig hubSpotConfig) {
        this.hubSpotConfig = hubSpotConfig;
    }

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthUrl() {
        String authUrl = hubSpotConfig.getHubSpotAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> oauthCallback(@RequestParam String code) {
        try {
            String clientId = hubSpotConfig.getClientId();
            String clientSecret = hubSpotConfig.getClientSecret();
            String redirectUri = hubSpotConfig.getRedirectUri();
            String tokenUrl = "https://api.hubapi.com/oauth/v1/token";

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
                    (int) (System.currentTimeMillis() / 1000) + tokenResponse.getExpiresIn()
            );

            return ResponseEntity.ok("Autenticação bem-sucedida! Access Token: " + tokenResponse.getAccessToken());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao processar o callback: " + e.getMessage());
        }
    }
}

