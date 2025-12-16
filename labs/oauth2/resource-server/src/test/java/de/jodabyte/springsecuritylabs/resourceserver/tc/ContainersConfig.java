package de.jodabyte.springsecuritylabs.resourceserver.tc;

import dasniko.testcontainers.keycloak.KeycloakContainer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.test.context.DynamicPropertyRegistrar;

@Slf4j
@TestConfiguration
public class ContainersConfig {

    @Value("${app.keycloak-image}")
    private String keycloakImage;

    @Value("${app.keycloak-realm-file}")
    private String keycloakRealmFile;

    @Value("${app.keycloak-realm-name}")
    private String keycloakRealmName;

    @Bean
    public KeycloakContainer keycloak() {
        var keycloak = new KeycloakContainer(keycloakImage).withRealmImportFile(keycloakRealmFile);
        return keycloak;
    }

    @Bean
    public DynamicPropertyRegistrar keycloakProperties(KeycloakContainer container) {
        String issuerUri = container.getAuthServerUrl() + "/realms/" + keycloakRealmName;
        log.info("Keycloak issuer-uri: " + issuerUri);

        return (properties) -> {
            properties.add(
                    "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                    () -> issuerUri
            );
        };
    }

    @Bean
    public JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) {
        return JwtDecoders.fromIssuerLocation(issuerUri);
    }
}
