package com.example.application.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties("consid.sales")
public class CRMConfig {

    private List<String> contacts;

    public List<String> getContacts() { return contacts; }

    public void setContacts(List<String> contacts) { this.contacts = contacts; }
}
