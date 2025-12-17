package de.jodabyte.springsecuritylabs.methodsecurity.callauthorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PreAuthorizeWithMethodParameterTest {

    @Autowired
    private AccessService sut;

    @Test
    @DisplayName("""
            Given a authorization rule secures the access to the method and expects the username of the authenticated user as a parameter
            When no user is authenticated
            Then throw an IllegalArgumentException.
            """)
    void testAccessWithNoUser() {
        assertThrows(IllegalArgumentException.class, () -> this.sut.canCallWithCorrectUsername("No User"));
    }

    @Test
    @DisplayName("""
            Given a authorization rule secures the access to the method and expects the username of the authenticated user as a parameter
            When a different user is provided that is authenticated
            Then throw an AuthorizationDeniedException.
            """)
    @WithMockUser("green")
    void testAccessWithWrongAuthority() {
        assertThrows(AuthorizationDeniedException.class, () -> this.sut.canCallWithCorrectUsername("blue"));
    }

    @Test
    @DisplayName("""
            Given a authorization rule secures the access to the method and expects the username of the authenticated user as a parameter
            When the same user is provided that is authenticated
            Then method returns the expected result.
            """)
    @WithMockUser("green")
    void testAccessWithRightAuthority() {
        assertEquals(AccessService.ACCESS_GRANTED, this.sut.canCallWithCorrectUsername("green"));
    }

}