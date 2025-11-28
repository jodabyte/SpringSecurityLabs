package de.jodabyte.springsecuritylabs.cors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cors")
public class CorsController {
    
    @GetMapping("/ping")
    public String getPing() {
        return "ping";
    }

    @PostMapping("/ping")
    public String postPing() {
        return "ping";
    }
}
