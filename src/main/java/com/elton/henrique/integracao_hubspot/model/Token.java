package com.elton.henrique.integracao_hubspot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String token_type;

    @Column(nullable = false)
    private String access_token;

    @Column(nullable = false)
    private String refresh_token;

    @Column(nullable = false, unique = true)
    private Integer expires_in;

    public Token(String token_type, String access_token, String refresh_token, int expires_in) {
        this.token_type = token_type;
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.expires_in = expires_in;
    }

    public Token(){
        this.token_type = "";
        this.access_token = "";
        this.refresh_token = "";
        this.expires_in = 0;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }
}