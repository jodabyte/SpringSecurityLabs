package de.jodabyte.springsecuritylabs.resourceserver;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/private")
    public String getPrivate() {
        return "Access Granted!";
    }
}
