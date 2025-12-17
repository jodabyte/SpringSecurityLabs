package de.jodabyte.springsecuritylabs.csrf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain defaultHttpSessionSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/defaults/**")
                .httpBasic(Customizer.withDefaults())
                .authorizeHttpRequests(c -> c.anyRequest().authenticated());

        return http.build();
    }

    @Bean
    public SecurityFilterChain singlePageApplicationsSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/spa/**")
                .httpBasic(Customizer.withDefaults())
                .csrf(CsrfConfigurer::spa)
                .authorizeHttpRequests(c -> c.anyRequest().authenticated());

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService() {
        var uds = new InMemoryUserDetailsManager();
        uds.createUser(MockUser.asUserDetails());
        return uds;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
