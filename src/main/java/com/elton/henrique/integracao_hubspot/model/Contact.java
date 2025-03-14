package com.elton.henrique.integracao_hubspot.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Contact {

    @JsonProperty("properties")
    private Properties properties;

    public static class Properties {
        @JsonProperty("email")
        private String email;

        @JsonProperty("firstname")
        private String firstName;

        @JsonProperty("lastname")
        private String lastName;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
