package de.jodabyte.springsecuritylabs.resourceserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.jodabyte.springsecuritylabs.resourceserver.tc.ContainersConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.RestClient;

@SpringBootTest
@Import(ContainersConfig.class)
@AutoConfigureMockMvc
@AutoConfigureRestTestClient
class ResourceServerE2ETests {

    @Autowired
    private RestTestClient client;

    @Test
    @DisplayName("""
            Given a protected resource
            When no jwt token is provided with the request
            Then return a 401.
            """)
    void testWitNoToken() throws Exception {
        this.client.get()
                .uri("/private")
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }

    @Test
    @DisplayName("""
            Given a protected resource
            When a valid jwt token is provided with the request
            Then return a 200.
            """)
    void testWithValidToken(@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri) throws Exception {
        String requestBody = "grant_type=client_credentials&client_id=web-client&client_secret=QAIRF2NmoY6jK7YYrae5kf50r7nqZfUK";
        RestClient tokenClient = RestClient.builder().baseUrl(issuerUri).build();

        // Request a token
        TokenResponse tokenResponse = tokenClient.post()
                .uri("/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .body(TokenResponse.class);

        // Request a protected resource using a valid token
        this.client.get()
                .uri("/private")
                .header("Authorization", "Bearer " + tokenResponse.accessToken)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(String.class)
                .isEqualTo("Access Granted!");

    }

    private record TokenResponse(@JsonProperty("access_token") String accessToken,
                                 @JsonProperty("expires_in") int expiresIn,
                                 @JsonProperty("refresh_expires_in") int refreshExpiresIn,
                                 @JsonProperty("token_type") String tokenType,
                                 @JsonProperty("not_before_policy") String notBeforePolicy,
                                 @JsonProperty("scope") String scope) {
    }
}
