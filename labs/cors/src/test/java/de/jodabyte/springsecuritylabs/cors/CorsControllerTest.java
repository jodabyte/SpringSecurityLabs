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
class CorsControllerTest {

    @Autowired
    private MockMvc sut;
    
    @Test
    @DisplayName("Test invalid http method for /cors/ping endpoint")
    public void testInvalidMethodForPingEndpoint() throws Exception {
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
    @DisplayName("Test invalid origin for /cors/ping endpoint")
    public void testInvalidOriginForPingEndpoint() throws Exception {
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
    @DisplayName("Test CORS configuration for /cors/ping endpoint")
    public void testCORSForPingEndpoint() throws Exception {
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
