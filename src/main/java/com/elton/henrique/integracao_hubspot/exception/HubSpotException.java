package com.elton.henrique.integracao_hubspot.exception;

import org.springframework.http.HttpStatus;

public class HubSpotException extends RuntimeException {
    private final HttpStatus status;

    public HubSpotException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
