package de.jodabyte.springsecuritylabs.csrf;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static de.jodabyte.springsecuritylabs.csrf.TestUtils.withHttpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DefaultCsrfControllerTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("""
            Given CSRF enabled /defaults/ping endpoint
            When using GET method
            Then respond with 200.
            """)
    void testCanCallGetMethod() throws Exception {
        this.sut.perform(get("/defaults/ping")
                        .with(withHttpBasic()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("""
            Given CSRF enabled /defaults/ping endpoint
            When using POST method without CSRF token
            Then respond with 403.
            """)
    void testCanNotCallPostMethod() throws Exception {
        this.sut.perform(post("/defaults/ping")
                        .with(withHttpBasic()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            Given CSRF enabled /defaults/ping endpoint
            When using POST method with a valid CSRF token
            Then respond with 200.
            """)
    void testCanCallPostMethod() throws Exception {
        this.sut.perform(post("/defaults/ping")
                        .with(withHttpBasic())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            Given CSRF enabled /defaults/ping endpoint
            When using POST method with a valid CSRF token in the header
            Then respond with 200.
            """)
    void testCanCallPostMethodWithHeader() throws Exception {
        this.sut.perform(post("/defaults/ping")
                        .with(withHttpBasic())
                        .with(csrf().asHeader()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            Given CSRF enabled /defaults/ping endpoint
            When using POST method with an invalid CSRF token
            Then respond with 403.
            """)
    void testCallPostMethodWithInvalidToken() throws Exception {
        this.sut.perform(post("/defaults/ping")
                        .with(withHttpBasic())
                        .with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
    }
}