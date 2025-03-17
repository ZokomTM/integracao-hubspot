package com.elton.henrique.integracao_hubspot.controller;

import com.elton.henrique.integracao_hubspot.exception.HubSpotException;
import com.elton.henrique.integracao_hubspot.services.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<String> createContactInHubSpot(@RequestBody String payload) {
        String response = contactService.createContact(payload);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/list")
    public ResponseEntity<String> getContacts() {
        String contacts = contactService.listContacts();
        return ResponseEntity.ok(contacts);
    }
}
