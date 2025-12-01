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
    @DisplayName("When the method is called with a user " +
            "but the returned object doesn't meet the authorization rules, " +
            "it should throw AuthorizationDeniedException")
    @WithMockUser(username = "blue")
    void testSearchingProtectedUser() {
        assertThrows(AuthorizationDeniedException.class,
                () -> this.sut.canReceiveMethodResultWithCorrectRole("green"));
    }

    @Test
    @DisplayName("When the method is called for a reader that doesn't have the reader role, " +
            "it should successfully return.")
    @WithMockUser(username = "green")
    void testSearchingUnprotectedUser() {
        var result = this.sut.canReceiveMethodResultWithCorrectRole("blue");

        var expected = new Employee("Blue",
                List.of("Karamazov Brothers"),
                List.of("accountant", "reader"));

        assertEquals(expected, result);
    }
}
