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
    @DisplayName("Call endpoint /defaults/ping using GET")
    void testCanCallGetMethod() throws Exception {
        this.sut.perform(get("/defaults/ping").with(withHttpBasic()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Call endpoint /defaults/ping using POST without providing the CSRF token")
    void testCanNotCallPostMethod() throws Exception {
        this.sut.perform(post("/defaults/ping").with(withHttpBasic()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Call endpoint /defaults/ping using POST providing the CSRF token")
    void testCanCallPostMethod() throws Exception {
        this.sut.perform(post("/defaults/ping").with(withHttpBasic()).with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Call endpoint /defaults/ping using POST providing the CSRF token in header")
    void testCanCallPostMethodWithHeader() throws Exception {
        this.sut.perform(post("/defaults/ping").with(withHttpBasic()).with(csrf().asHeader()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Call endpoint /defaults/ping using POST providing an invalid CSRF token")
    void testCallPostMethodWithInvalidToken() throws Exception {
        this.sut.perform(post("/defaults/ping").with(withHttpBasic()).with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
    }
}