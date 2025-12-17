package de.jodabyte.springsecuritylabs.authorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
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
    @DisplayName("""
            Given a authority protected endpoint
            When no user is provided
            Then no authentication object is created
            """)
    void testFailedAuthentication() throws Exception {
        this.sut.perform(get("/authorities/read"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("""
            Given a the user c3po
            When user cannot access /authorities/read but can access /authorities/write
            Then respond with the correct HTTP codes.
            """)
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
    @DisplayName("""
            Given a the user r2d2
            When user can access /authorities/read and /authorities/write
            Then respond with the correct HTTP codes.
            """)
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
    @DisplayName("""
            Given a the user c3po
            When user can access /authorities/delete
            Then respond with 200.
            """)
    @WithUserDetails("c3po")
    void testExpressionBasedAuthority() throws Exception {
        this.sut.perform(get("/authorities/delete"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            Given a the user r2d2
            When user cannot access /authorities/delete if user has an authority that is not allowed
            Then respond with 403.
            """)
    @WithUserDetails("r2d2")
    void testFailExpressionBasedAuthority() throws Exception {
        this.sut.perform(get("/authorities/delete"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }
}