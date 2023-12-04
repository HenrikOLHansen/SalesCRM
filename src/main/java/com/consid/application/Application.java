package com.consid.application;

import com.consid.application.config.CRMConfig;
import com.consid.application.data.service.CrmService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(CRMConfig.class)
@Theme(value = "salescrm", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {

    private final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner config(final CRMConfig config, final CrmService crmService) {
        return args -> {
            logger.info("Loading configuration properties...");
            logger.info("Found {} Consid contact names", config.getContacts().size());
            crmService.saveConsidContacts(config.getContacts());
        };
    }

}
