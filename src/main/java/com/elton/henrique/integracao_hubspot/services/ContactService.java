package com.elton.henrique.integracao_hubspot.services;

import com.elton.henrique.integracao_hubspot.config.HubSpotConfig;
import com.elton.henrique.integracao_hubspot.exception.HubSpotException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class ContactService {

    @Autowired
    private HubSpotConfig hubSpotConfig;
    @Autowired
    private TokenService tokenService;

    private final RestTemplate restTemplate = new RestTemplate();

    public String createContact(String payload) {
        String accessToken = tokenService.getValidAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(hubSpotConfig.getHubspotUrlContact(), HttpMethod.POST, request, String.class);
            return response.getBody();

        } catch (HttpClientErrorException e) {
            HttpStatus status = (HttpStatus) e.getStatusCode();
            String responseBody = e.getResponseBodyAsString();

            throw new HubSpotException("Erro ao integrar com HubSpot: " + responseBody, status);
        } catch (Exception e) {
            throw new HubSpotException("Erro inesperado ao criar contato no HubSpot.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String listContacts() {
        String accessToken = tokenService.getValidAccessToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{}";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(hubSpotConfig.getHubspotUrlContact()+"/search", HttpMethod.POST, request, String.class);
            return response.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus status = (HttpStatus) e.getStatusCode();
            String responseBody = e.getResponseBodyAsString();

            throw new HubSpotException("Erro ao listar contatos: " + responseBody, status);
        } catch (Exception e) {
            throw new HubSpotException("Erro inesperado ao listar contatos no HubSpot.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
