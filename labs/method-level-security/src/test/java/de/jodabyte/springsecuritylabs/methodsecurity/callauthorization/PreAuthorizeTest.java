package de.jodabyte.springsecuritylabs.methodsecurity.callauthorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PreAuthorizeTest {

    @Autowired
    private AccessService sut;

    @Test
    @DisplayName("When the method is called without an authenticated user, " +
            "it throws AuthenticationException")
    void testAccessWithNoUser() {
        assertThrows(AuthenticationException.class, () -> this.sut.canCallWithWriteAuthority());
    }

    @Test
    @WithMockUser(authorities = "read")
    @DisplayName("When the method is called with an authenticated user having a wrong authority, " +
            "it throws AuthorizationDeniedException")
    void testAccessWithWrongAuthority() {
        assertThrows(AuthorizationDeniedException.class, () -> this.sut.canCallWithWriteAuthority());
    }

    @Test
    @WithMockUser(authorities = "write")
    @DisplayName("When the method is called with an authenticated user having a correct authority, " +
            "it returns the expected result")
    void testAccessWithRightAuthority() {
        assertEquals(AccessService.ACCESS_GRANTED, this.sut.canCallWithWriteAuthority());
    }

}