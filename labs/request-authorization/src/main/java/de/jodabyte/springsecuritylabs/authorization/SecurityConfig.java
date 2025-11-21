package de.jodabyte.springsecuritylabs.authorization;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

import javax.sql.DataSource;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasRole;
import static org.springframework.security.authorization.AuthorizationManagers.allOf;
import static org.springframework.security.authorization.AuthorizationManagers.not;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(Customizer.withDefaults());

        var expressionBasedAuthorization = new WebExpressionAuthorizationManager("hasAuthority('DELETE') and !hasAuthority('READ')");
        http.authorizeHttpRequests(c -> c.requestMatchers("/authorities/read").hasAuthority("READ")
                .requestMatchers("/authorities/write").hasAnyAuthority("READ", "WRITE")
                .requestMatchers("/authorities/delete").access(expressionBasedAuthorization)
                .requestMatchers("/roles/read").hasRole("USER")
                .requestMatchers("/roles/write").access(allOf(hasRole("ADMIN"), not(hasRole("MANAGER"))))
                .anyRequest().denyAll()
        );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        JdbcDaoImpl jdbcDao = new JdbcDaoImpl();
        jdbcDao.setDataSource(dataSource);
        return jdbcDao;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
