package de.jodabyte.springsecuritylabs.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HasAuthorityBasedTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("The endpoint cannot be called unauthenticated")
    void testFailedAuthentication() throws Exception {
        this.sut.perform(get("/authorities/read"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("User cannot access /authorities/read but can access /authorities/write")
    @WithUserDetails("c3po")
    void testHasAuthorityWriteButNotRead() throws Exception {
        this.sut.perform(get("/authorities/read"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());

        this.sut.perform(get("/authorities/write"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("A user can access /authorities/read and /authorities/write")
    @WithUserDetails("r2d2")
    void testHasAuthorityWriteAndRead() throws Exception {
        this.sut.perform(get("/authorities/read"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        this.sut.perform(get("/authorities/write"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("A user can access /authorities/delete with the necessary authority")
    @WithUserDetails("c3po")
    void testExpressionBasedAuthority() throws Exception {
        this.sut.perform(get("/authorities/delete"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("A user cannot access /authorities/delete if user has an authority that is not allowed")
    @WithUserDetails("r2d2")
    void testFailExpressionBasedAuthority() throws Exception {
        this.sut.perform(get("/authorities/delete"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }
}