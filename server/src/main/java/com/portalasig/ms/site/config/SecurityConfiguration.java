package com.portalasig.ms.site.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configures security settings for the application, including OAuth2 JWT authentication
 * and default form login behavior.
 */
@Configuration
public class SecurityConfiguration {

    @Value("${portalasig.security.oauth2.authorization-server.jwt.issuer-uri}")
    private String jwtIssuerUri;

    @Bean
    SecurityFilterChain clientSecurityFilterChain(HttpSecurity http) throws Exception {
        http.formLogin(Customizer.withDefaults());
        http.authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(oauth -> oauth.jwt(
                jwt -> jwt.decoder(JwtDecoders.fromIssuerLocation(jwtIssuerUri)
                )));
        return http.build();
    }
}
