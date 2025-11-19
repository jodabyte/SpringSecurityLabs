package de.jodabyte.springsecuritylabs.usernamepasswordauthentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BasicAuthenticationTests {

    private static final String ENDPOINT = "/ping";
    private static final String USERNAME = "r2d2";
    private static final String PASSWORD = "legend1";

    @Autowired
    private MockMvc mvc;

    @Test
    public void failCallingEndpointWithoutAuthentication() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void failCallingEndpointWitUnknownUser() throws Exception {
        mvc.perform(get(ENDPOINT).with(httpBasic("unknown user", "unknown password")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void successCallingEndpointWitCorrectUser() throws Exception {
        mvc.perform(get(ENDPOINT).with(httpBasic(USERNAME, PASSWORD)))
                .andExpect(status().isOk());
    }
}
