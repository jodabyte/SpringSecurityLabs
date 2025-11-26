package de.jodabyte.springsecuritylabs.csrf;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/spa")
public class SpaCsrfController {

    @GetMapping("/ping")
    public String getPing() {
        return "pong";
    }

    @PostMapping("/ping")
    public String postPing() {
        return "pong";
    }

}
