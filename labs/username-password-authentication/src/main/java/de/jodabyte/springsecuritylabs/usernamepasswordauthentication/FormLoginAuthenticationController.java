package de.jodabyte.springsecuritylabs.usernamepasswordauthentication;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FormLoginAuthenticationController {

    @GetMapping("/home")
    public String home() {
        return "home.html";
    }
}
