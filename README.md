# SpringSecurityLabs

## cors (cross-origin resource sharing)

Allow to specify what kind of cross-domain requests are authorized.

Uses:

- Global configuration to specify the allowed http methods (`POST`) and origins (`http://ping.de`).
- `@CrossOrigin` to specify fine-grained controller and controller method level control.

## csrf (cross site request forgery)

Protection against CSRF attacks.

Uses:

- `InMemoryUserDetailsManager` and `NoOpPasswordEncoder` to create user for tests
- Default CSRF setting that stores the expected CSRF token in a [HttpSession by using HttpSessionCsrfTokenRepository](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-token-repository-httpsession) and reads the actual token from a HTTP parameter `_csrf`
- [Single-Page Applications setting](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-integration-javascript-spa) that stores the expected CSRF token in a [cookie](https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#csrf-token-repository-cookie) named `XSRF-TOKEN` and reads the actual token from a HTTP request header `X-XSRF-TOKEN` or the request parameter `_csrf`

## request-authorization

Request based authorization using authorities and roles.

Uses:

- `JdbcDaoImpl` and `NoOpPasswordEncoder` to create user for tests
- [Authorization rules built into the DSL](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html#authorize-requests) to define authorization checks for endpoints:
  - `permitAll`, `denyAll`, `hasAuthority`, `hasRole`, `hasAnyAuthority`, `hasAnyRole`, `hasAllRoles`, `hasAllAuthorities` and `access`
- WebExpressionAuthorizationManager: to help migrate legacy SpEL (**It is recommended that you use type-safe authorization managers instead of SpEL**)
- AuthorizationManagers: helpful static factories for composing individual `AuthorizationManagers` into more sophisticated expressions.

## username-password-authentication

Authenticate an user by validating an username and password.

Uses:

- Basic Authentication
- Form Login Authentication
- `JdbcUserDetailsManager` to manage user and authorities
- `DelegatingPasswordEncoder` to support multiple password encoding schemes



