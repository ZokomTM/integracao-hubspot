package com.elton.henrique.integracao_hubspot.controller;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.exception.OAuthException;
import com.elton.henrique.integracao_hubspot.responses.TokenResponse;
import com.elton.henrique.integracao_hubspot.services.AuthService;
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

    @Autowired
    private HubSpotConfig hubSpotConfig;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthService authService;

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthUrl() {
        String authUrl = hubSpotConfig.getHubSpotAuthUrl();

        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> oauthCallback(@RequestParam String code) {
        try {
            authService.getAccessToken(code);
            return ResponseEntity.ok("");
        } catch (Exception e) {
            throw new OAuthException("Erro ao receber token de acesso do HubSpot: " + e.getMessage());
        }
    }
}

