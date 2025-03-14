package com.elton.henrique.integracao_hubspot.services;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.model.Contact;
import com.elton.henrique.integracao_hubspot.model.Token;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class ContactService {

    private final TokenService tokenService;
    private final HubSpotConfig hubSpotConfig;
    private final RestTemplate restTemplate = new RestTemplate();

    public ContactService(TokenService tokenService, HubSpotConfig hubSpotConfig) {
        this.tokenService = tokenService;
        this.hubSpotConfig = hubSpotConfig;
    }

    public ResponseEntity<String> createContact(Contact contact) {
        String accessToken = tokenService.getValidAccessToken();

        String url = "https://api.hubapi.com/contacts/v1/contact?hapikey=" + hubSpotConfig.getApiKey();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + accessToken);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String contactJson = objectMapper.writeValueAsString(contact);
            HttpEntity<String> request = new HttpEntity<>(contactJson, headers);
            return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar o contato", e);
        }
    }
}
