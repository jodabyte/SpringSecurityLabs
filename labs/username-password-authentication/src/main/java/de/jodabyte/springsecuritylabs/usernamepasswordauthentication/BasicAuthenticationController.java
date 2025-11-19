package de.jodabyte.springsecuritylabs.usernamepasswordauthentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicAuthenticationController {

    @GetMapping("/ping")
    public String ping() {
        return "Pong!";
    }
}
