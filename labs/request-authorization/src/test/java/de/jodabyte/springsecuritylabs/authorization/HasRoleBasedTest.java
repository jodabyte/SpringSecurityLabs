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
class HasRoleBasedTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("The endpoint cannot be called unauthenticated")
    void testFailedAuthentication() throws Exception {
        this.sut.perform(get("/roles/read"))
                .andExpect(unauthenticated());
    }

    @Test
    @DisplayName("User can access /roles/read but cannot access /roles/write")
    @WithUserDetails("c3po")
    void testHasRoleToRead() throws Exception {
        this.sut.perform(get("/roles/read"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        this.sut.perform(get("/roles/write"))
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("A user can access /roles/read and /roles/write")
    @WithUserDetails("r2d2")
    void testHasRoleToWriteAndRead() throws Exception {
        this.sut.perform(get("/roles/read"))
                .andExpect(authenticated())
                .andExpect(status().isOk());

        this.sut.perform(get("/roles/write"))
                .andExpect(authenticated())
                .andExpect(status().isOk());
    }
}
