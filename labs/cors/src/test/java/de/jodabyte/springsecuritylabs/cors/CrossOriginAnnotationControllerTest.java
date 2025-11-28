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
class CrossOriginAnnotationControllerTest {

    @Autowired
    private MockMvc sut;

    @Test
    @DisplayName("Test no CORS configuration for /annotation/ping endpoint")
    public void testNoCORSForPingEndpoint() throws Exception {
        sut.perform(options("/annotation/ping")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://pong.de")
                )
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Test invalid origin for /annotation/pong endpoint")
    public void testInvalidOriginForPongEndpoint() throws Exception {
        sut.perform(options("/annotation/pong")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://pong.com")
                )
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"))
                .andExpect(header().doesNotExist("Access-Control-Allow-Methods"))
                .andExpect(content().string("Invalid CORS request"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Test CORS configuration for /annotation/pong endpoint")
    public void testCORSForPongEndpoint() throws Exception {
        sut.perform(options("/annotation/pong")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Origin", "http://pong.de")
                )
                .andExpect(header().exists("Access-Control-Allow-Origin"))
                .andExpect(header().string("Access-Control-Allow-Origin", "http://pong.de"))
                .andExpect(header().exists("Access-Control-Allow-Methods"))
                .andExpect(header().string("Access-Control-Allow-Methods", "POST"))
                .andExpect(status().isOk());
    }
}
