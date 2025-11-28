package de.jodabyte.springsecuritylabs.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain crossOriginAnnotationSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/annotation/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(r -> r.anyRequest().permitAll());

        return http.build();
    }
    
    @Bean
    public SecurityFilterChain corsSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/cors/**")
                .cors(c -> {
                    CorsConfigurationSource source = request -> {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(List.of("http://ping.de"));
                        config.setAllowedMethods(List.of("POST"));
                        return config;
                    };
                    c.configurationSource(source);
                });

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                c -> c.anyRequest().permitAll()
        );

        return http.build();
    }

}
