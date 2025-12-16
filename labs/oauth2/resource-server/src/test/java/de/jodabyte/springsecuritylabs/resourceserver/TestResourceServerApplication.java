package de.jodabyte.springsecuritylabs.resourceserver;

import de.jodabyte.springsecuritylabs.resourceserver.tc.ContainersConfig;
import org.springframework.boot.SpringApplication;

public class TestResourceServerApplication {

    public static void main(String[] args) {
        SpringApplication
                .from(ResourceServerApplication::main)
                .with(ContainersConfig.class)
                .run(args);
    }
}
