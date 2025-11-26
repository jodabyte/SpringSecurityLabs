package de.jodabyte.springsecuritylabs.usernamepasswordauthentication;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FormLoginAuthenticationTests {

    private static final String USERNAME = "r2d2";
    private static final String PASSWORD = "legend1";

    @Autowired
    private MockMvc mvc;

    @Test
    public void failToLoginWhenUsingWrongUser() throws Exception {
        mvc.perform(formLogin().user("unknown user").password("unknown password"))
                .andExpect(unauthenticated());
    }

    @Test
    public void successToLoginAndLogoutWithCorrectUser() throws Exception {
        mvc.perform(formLogin().user(USERNAME).password(PASSWORD))
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().isFound())
                .andExpect(authenticated());

        mvc.perform(logout())
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().isFound())
                .andExpect(unauthenticated());
    }
}
