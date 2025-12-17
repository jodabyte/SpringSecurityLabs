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
    @DisplayName("""
            Given a authorization rule secures the access to the method
            When no user is authenticated
            Then throw an AuthenticationException.
            """)
    void testAccessWithNoUser() {
        assertThrows(AuthenticationException.class, () -> this.sut.canCallWithWriteAuthority());
    }

    @Test
    @DisplayName("""
            Given a authorization rule secures the access to the method
            When the user does not have the necessary authority
            Then throw an AuthorizationDeniedException.
            """)
    @WithMockUser(authorities = "read")
    void testAccessWithWrongAuthority() {
        assertThrows(AuthorizationDeniedException.class, () -> this.sut.canCallWithWriteAuthority());
    }

    @Test
    @DisplayName("""
            Given a authorization rule secures the access to the method
            When the user does have the necessary authority
            Then method returns the expected result.
            """)
    @WithMockUser(authorities = "write")
    void testAccessWithRightAuthority() {
        assertEquals(AccessService.ACCESS_GRANTED, this.sut.canCallWithWriteAuthority());
    }

}