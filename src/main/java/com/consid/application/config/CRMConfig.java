package com.consid.application.config;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "consid.sales")
public class CRMConfig {

    private List<String> contacts;

}
