package com.portalasig.ms.site;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Entry point for the Site microservice application.
 * Enables JPA auditing and scans base packages for Spring components.
 */
@EnableJpaAuditing
@EnableJpaRepositories
@SpringBootApplication(scanBasePackages = {"com.portalasig.ms.commons", "com.portalasig.ms.site"})
public class ServerApplication {

    /**
     * Starts the Spring Boot application.
     *
     * @param args application arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
