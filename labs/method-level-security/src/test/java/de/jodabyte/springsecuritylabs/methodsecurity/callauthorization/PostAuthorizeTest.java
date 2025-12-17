package de.jodabyte.springsecuritylabs.methodsecurity.callauthorization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PostAuthorizeTest {

    @Autowired
    private AccessService sut;

    @Test
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When user role does not match the rules
            Then throw an AuthorizationDeniedException.
            """)
    @WithMockUser(username = "blue")
    void testSearchingProtectedUser() {
        assertThrows(AuthorizationDeniedException.class,
                () -> this.sut.canReceiveMethodResultWithCorrectRole("green"));
    }

    @Test
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When user role does match the rules
            Then return the object.
            """)
    @WithMockUser(username = "green")
    void testSearchingUnprotectedUser() {
        var result = this.sut.canReceiveMethodResultWithCorrectRole("blue");

        var expected = new Employee("Blue",
                List.of("Karamazov Brothers"),
                List.of("accountant", "reader"));

        assertEquals(expected, result);
    }
}
