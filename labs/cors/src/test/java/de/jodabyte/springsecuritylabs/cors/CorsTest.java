package de.jodabyte.springsecuritylabs.cors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CorsTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("""
            Given CORS protected /cors/ping endpoint
            When using the not allowed GET method and an allowed origin
            Then respond with 403.
            """)
    public void testInvalidMethod() throws Exception {
        sut.perform(options("/cors/ping")
                        .header("Access-Control-Request-Method", "GET")
                        .header("Origin", "http://ping.de")
                )
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"))
                .andExpect(content().string("Invalid CORS request"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            Given CORS protected /cors/ping endpoint
            When using an allowed POST method and a not allowed origin
            Then respond with 403.
            """)
    public void testInvalidOrigin() throws Exception {
        sut.perform(options("/cors/ping")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://ping.com")
                )
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"))
                .andExpect(content().string("Invalid CORS request"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("""
            Given CORS protected /cors/ping endpoint
            When using an allowed POST method and an allowed origin
            Then respond with 200.
            """)
    public void testValidCORS() throws Exception {
        sut.perform(options("/cors/ping")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://ping.de")
                )
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://ping.de"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
                .andExpect(status().isOk());
    }
}
