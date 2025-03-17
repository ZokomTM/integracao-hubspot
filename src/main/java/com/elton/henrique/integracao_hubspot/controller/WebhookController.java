package com.elton.henrique.integracao_hubspot.controller;

import com.elton.henrique.integracao_hubspot.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/contact-creation")
    public ResponseEntity<String> receiveContactCreationEvent(@RequestBody String payload) {
        return ResponseEntity.ok("Recebido com sucesso" + payload);
    }
}
