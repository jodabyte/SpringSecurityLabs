package de.jodabyte.springsecuritylabs.resourceserver;

import de.jodabyte.springsecuritylabs.resourceserver.tc.ContainersConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Import(ContainersConfig.class)
@AutoConfigureMockMvc
class ResourceServerIntegrationTests {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("Given no jwt token is provided with the request" +
            "When accessing a protected resource" +
            "Then return a 401.")
    void testWitNoToken() throws Exception {
        this.sut.perform(get("/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Given a valid jwt token is provided with the request" +
            "When accessing a protected resource" +
            "Then return a 200.")
    void testWithValidToken() throws Exception {
        this.sut.perform(get("/private").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(content().string("Access Granted!"));
    }

}
