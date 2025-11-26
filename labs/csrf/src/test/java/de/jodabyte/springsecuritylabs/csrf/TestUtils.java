package de.jodabyte.springsecuritylabs.csrf;

import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public class TestUtils {

    public static RequestPostProcessor withHttpBasic() {
        return httpBasic(MockUser.USERNAME, MockUser.PASSWORD);
    }
}
