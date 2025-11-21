# SpringSecurityLabs

## request-authorization

Request based authorization using authorities and roles

- Use `JdbcDaoImpl` and `NoOpPasswordEncoder` for testing purposes
- Use [authorization rules built into the DSL](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests):
  - `permitAll`, `denyAll`, `hasAuthority`, `hasRole`, `hasAnyAuthority`, `hasAnyRole`, `hasAllRoles`, `hasAllAuthorities` and `access`
- WebExpressionAuthorizationManager: to help migrate legacy SpEL (**It is recommended that you use type-safe authorization managers instead of SpEL**)
- AuthorizationManagers: helpful static factories for composing individual `AuthorizationManagers` into more sophisticated expressions.


## username-password-authentication

Authenticate an user by validating an username and password.

- Basic Authentication
- Form Login Authentication
- `JdbcUserDetailsManager` to manage user and authorities
- `DelegatingPasswordEncoder` to support multiple password encoding schemes



