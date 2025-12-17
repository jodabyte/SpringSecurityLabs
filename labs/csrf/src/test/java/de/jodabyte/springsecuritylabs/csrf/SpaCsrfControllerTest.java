package de.jodabyte.springsecuritylabs.csrf;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static de.jodabyte.springsecuritylabs.csrf.TestUtils.withHttpBasic;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpaCsrfControllerTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("""
            Given CSRF enabled /spa/ping endpoint
            When using GET method
            Then respond with 200.
            """)
    void testCanCallGetMethod() throws Exception {
        this.sut.perform(get("/spa/ping")
                        .with(withHttpBasic()))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("""
            Given CSRF enabled /spa/ping endpoint
            When using POST method and without providing the CSRF token
            Then respond with 403.
            """)
    void testCannotCallPostMethodWithoutToken() throws Exception {
        this.sut.perform(post("/spa/ping")
                        .with(withHttpBasic()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            Given CSRF enabled /spa/ping endpoint
            When using POST method and a valid CSRF token
            Then respond with 200.
            """)
    void testCanCallPostMethodWithToken() throws Exception {
        this.sut.perform(post("/spa/ping")
                        .with(withHttpBasic())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    /**
     * {@link SecurityMockMvcRequestPostProcessors.CsrfRequestPostProcessor#asHeader()} does not work when csrf is configured
     * with single page application support {@link CsrfConfigurer#spa()}. Therefore, a GET request is required to get the
     * cookie with the token.
     */
    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @DisplayName("""
            Given CSRF enabled /spa/ping endpoint
            When using POST method and a valid CSRF token in header
            Then respond with 200.
            """)
    void testCanCallPostMethodWithHeader() throws Exception {
        Cookie cookie = this.sut.perform(get("/spa/ping")
                        .with(withHttpBasic()))
                .andReturn().getResponse().getCookie("XSRF-TOKEN");

        this.sut.perform(post("/spa/ping")
                        .with(withHttpBasic())
                        .header("X-XSRF-TOKEN", cookie.getValue())
                        .cookie(cookie))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("""
            Given CSRF enabled /spa/ping endpoint
            When using POST method and a invalid CSRF token
            Then respond with 403.
            """)
    void testCallPostMethodWithInvalidToken() throws Exception {
        this.sut.perform(post("/spa/ping")
                        .with(withHttpBasic())
                        .with(csrf().useInvalidToken()))
                .andExpect(status().isForbidden());
    }


}