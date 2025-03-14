package com.elton.henrique.integracao_hubspot.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/webhooks")
public class WebhookController {

    @PostMapping("/contact-creation")
    public ResponseEntity<String> receiveContactCreationEvent(@RequestBody String payload) {

        System.out.println("Evento de criação de contato recebido: " + payload);
        return ResponseEntity.ok("Recebido com sucesso");
    }
}
