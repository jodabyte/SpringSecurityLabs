package de.jodabyte.springsecuritylabs.authorization;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/authorities")
public class AuthorityBasedController {

    @GetMapping("/write")
    public String write() {
        return "write";
    }

    @GetMapping("/read")
    public String read() {
        return "read";
    }

    @GetMapping("/delete")
    public String delete() {
        return "delete";
    }
}
