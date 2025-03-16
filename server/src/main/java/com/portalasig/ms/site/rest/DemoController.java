package com.portalasig.ms.site.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.portalasig.ms.notify.client.EmailNotifyClient;
import com.portalasig.ms.notify.constant.EmailTemplate;
import com.portalasig.ms.notify.dto.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/demo")
@RequiredArgsConstructor
public class DemoController {

    @Qualifier("emailNotifyClientV1")
    private final EmailNotifyClient emailNotifyClient;
    private final ObjectMapper objectMapper;

    @GetMapping("/user")
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    public ResponseEntity<String> helloUser() {
        return ResponseEntity.ok("Hello, role user");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Hello, role ADMIN!!");
    }

    @GetMapping("/test-client")
    public void testClient() throws JsonProcessingException {
        String simpleMessage = """
                {
                  "by": "Frank test ms-site",
                  "message": "el body"
                }
                """;
        EmailRequest emailRequest = EmailRequest
                .builder()
                .emailTo("frankponte95@gmail.com")
                .subject("soy yo marico")
                .template(EmailTemplate.SIMPLE_MESSAGE)
                .templateConfiguration(objectMapper.readTree(simpleMessage))
                .build();
        emailNotifyClient.sendApplicationEmail(emailRequest).block();
    }
}
