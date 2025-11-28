package de.jodabyte.springsecuritylabs.cors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/annotation")
public class CrossOriginAnnotationController {
    
    @PostMapping("/ping")
    public String ping() {
        return "ping";
    }

    @PostMapping("/pong")
    @CrossOrigin("http://pong.de")
    public String pong() {
        return "pong";
    }

}
