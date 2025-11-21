package de.jodabyte.springsecuritylabs.authorization;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/roles")
public class RoleBasedController {

    @GetMapping("/read")
    public String read() {
        return "read";
    }

    @GetMapping("/write")
    public String write() {
        return "write";
    }
}
