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

## method-level-security

Method based authorization using authorities and roles.

Uses:

- `InMemoryUserDetailsManager` and `NoOpPasswordEncoder` to create user for tests.
- `@PreAuthorize` and ` @PostAuthorize` for method level access control.
- [Expressing Authorization with SpEL](https://docs.spring.io/spring-security/reference/servlet/authorization/method-security.html#authorization-expressions): 
  - Methods: `permitAll`, `denyAll`, `hasAuthority`, `hasRole`, `hasAnyAuthority`, `hasAnyRole`, `hasAllRoles`, `hasAllAuthorities` and `hasPermission`.
  - Fields: `authentication` and `principal`.
- Method parameters and return values, they can also be accessed in the SpEL expression.
- `PermissionEvaluator` to create custom evaluation expressions.

## oauth2

### authorization-server

Build an OAuth 2/OpenID Connect authorization server from scratch with spring security authorization server framework.

Uses:

- `JdbcUserDetailsManager` to manage user and authorities.
- `DelegatingPasswordEncoder` to support multiple password encoding schemes.
- `JdbcRegisteredClientRepository` to manage OAuth2 clients.
- Enable Authorization Grant Types: `AUTHORIZATION_CODE`, `CLIENT_CREDENTIALS`.
- Enable Proof Key for Code Exchange (`PKCE`).
- `JWKSource` for signing access tokens using a `JWKSet` generated on startup.
- Non-Opaque Tokens (JSON Web Tokens).
- `MockMvc` support for spring security in integration tests.
- `org.htmlunit.WebClient` for End-To-End tests.

### resource-server

Build a backend service with spring security resource server framework.

Uses:

- [Keycloak Module as Testcontainer](https://testcontainers.com/modules/keycloak/).
- Built-in support in spring boot to manage testcontainers as spring beans.
- `MockMvc` support for spring security in integration tests and end-to-end tests.
- `TestResourceServerApplication` to create a [local environment with testcontainers](https://www.baeldung.com/spring-boot-built-in-testcontainers#testcontainers-support-for-local-development).

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

# Appendix

## Deploy a keycloak and export the realm config

1. [Get started with Keycloak on Docker](https://www.keycloak.org/getting-started/getting-started-docker#_start_keycloak)

2. [Securing Spring Boot Microservice using Keycloak and Testcontainers](https://testcontainers.com/guides/securing-spring-boot-microservice-using-keycloak-and-testcontainers/)

   - [Workaround to export a realm when `start-dev` parameter is used](https://github.com/keycloak/keycloak/issues/33800#issuecomment-2411056817)
   - Add `--users realm_file` to include the users into the export

    > docker exec -it my_keycloak sh -c \
    >  "cp -rp /opt/keycloak/data/h2 /tmp ; \
    >  /opt/keycloak/bin/kc.sh export --dir /opt/keycloak/data/import --users realm_file --realm MY_REALM \
    >    --db dev-file \
    >    --db-url 'jdbc:h2:file:/tmp/h2/keycloakdb;NON_KEYWORDS=VALUE'"