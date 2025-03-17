package com.elton.henrique.integracao_hubspot.services;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.exception.OAuthException;
import com.elton.henrique.integracao_hubspot.exception.TokenException;
import com.elton.henrique.integracao_hubspot.exception.TokenNotFoundException;
import com.elton.henrique.integracao_hubspot.model.Token;
import com.elton.henrique.integracao_hubspot.repository.TokenRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import java.time.Instant;

@Service
public class TokenService {
    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private HubSpotConfig hubSpotConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public Token getToken() {
        Token token = tokenRepository.findFirstByOrderByIdDesc();
        return token;
    }

    @Transactional
    public void saveToken(Token newToken) {
        newToken.setExpires_in((int) (System.currentTimeMillis() / 1000) + newToken.getExpires_in());
        tokenRepository.save(newToken);
    }

    public void saveToken(String tokenType, String accessToken, String refreshToken, Integer expiresIn) {
        Token newToken = new Token(tokenType, accessToken, refreshToken, expiresIn);
        saveToken(newToken);
    }

    public boolean isTokenExpired(Token token) {
        if (token == null) {
            return true;
        }
        boolean expired = Instant.now().getEpochSecond() >= token.getExpires_in();
        return expired;
    }

    public String getValidAccessToken() {
        Token token = getToken();
        if (isTokenExpired(token)) {
            token = refreshAccessToken(token);
        }
        return token.getAccess_token();
    }

    public Token refreshAccessToken(Token token) {
        if (token == null || token.getRefresh_token() == null) {
            throw new TokenNotFoundException("Nenhum token válido encontrado para renovação.");
        }

        String tokenUrl = "https://api.hubapi.com/oauth/v1/token";
        String body = "grant_type=refresh_token" +
                "&client_id=" + hubSpotConfig.getClientId() +
                "&client_secret=" + hubSpotConfig.getClientSecret() +
                "&refresh_token=" + token.getRefresh_token();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        try {
            HttpEntity<String> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, request, String.class);

            return processTokenResponse(response);
        } catch (Exception ex) {
            throw new OAuthException("Erro ao obter o token do HubSpot: " + ex.getMessage());
        }
    }

    public Token processTokenResponse(ResponseEntity<String> response) {
        try {
            JsonNode jsonNode = parseResponseBody(response.getBody());

            validateTokenFields(jsonNode);

            Token newToken = extractTokenFromJson(jsonNode);

            saveToken(newToken);

            return newToken;
        } catch (JsonProcessingException e) {
            throw new TokenException("Erro ao processar JSON da resposta: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new TokenException("Erro ao atualizar token: " + e.getMessage(), e);
        }
    }

    private JsonNode parseResponseBody(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(responseBody);
    }

    private void validateTokenFields(JsonNode jsonNode) {
        if (!jsonNode.has("token_type") || !jsonNode.has("access_token")
                || !jsonNode.has("refresh_token") || !jsonNode.has("expires_in")) {
            throw new TokenException("Resposta da API não contém todos os campos esperados.");
        }
    }

    private Token extractTokenFromJson(JsonNode jsonNode) {
        return new Token(
                jsonNode.get("token_type").asText(),
                jsonNode.get("access_token").asText(),
                jsonNode.get("refresh_token").asText(),
                jsonNode.get("expires_in").asInt()
        );
    }
}
