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
    @DisplayName("When the method is called without a user," +
            " it throws IllegalArgumentException")
    void testAccessWithNoUser() {
        assertThrows(IllegalArgumentException.class, () -> this.sut.canCallWithCorrectUsername("No User"));
    }

    @Test
    @WithMockUser("green")
    @DisplayName("When the method is called with a different username parameter than the authenticated user, " +
            "it should throw AuthorizationDeniedException.")
    void testAccessWithWrongAuthority() {
        assertThrows(AuthorizationDeniedException.class, () -> this.sut.canCallWithCorrectUsername("blue"));
    }

    @Test
    @WithMockUser("green")
    @DisplayName("When the method is called for the authenticated user, " +
            "it should return the expected result.")
    void testAccessWithRightAuthority() {
        assertEquals(AccessService.ACCESS_GRANTED, this.sut.canCallWithCorrectUsername("green"));
    }

}