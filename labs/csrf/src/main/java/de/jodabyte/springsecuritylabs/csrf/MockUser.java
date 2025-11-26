package de.jodabyte.springsecuritylabs.csrf;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

public class MockUser {

    public final static String USERNAME = "r2d2";
    public final static String PASSWORD = "legend1";

    public static UserDetails asUserDetails() {
        return User.withUsername(MockUser.USERNAME)
                .password(MockUser.PASSWORD)
                .build();
    }
}
