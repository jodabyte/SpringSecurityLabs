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
class PostAuthorizeWithPermissionEvaluatorTest {

    @Autowired
    private AccessService sut;

    @Test
    @DisplayName("When the method is called without a user, " +
            "it throws AuthenticationException")
    void testWithNoUser() {
        assertThrows(AuthenticationException.class,
                () -> this.sut.canReceiveMethodResultUsingPermissionEvaluator("abc123"));
    }

    @Test
    @WithMockUser(username = "green", roles = "manager")
    void testWithManagerRole() {
        assertThrows(AuthorizationDeniedException.class,
                () -> this.sut.canReceiveMethodResultUsingPermissionEvaluator("abc123"));
    }

    @Test
    @WithMockUser(username = "green", roles = "manager")
    void testWithManagerRoleForOwnUserDocument() {
        Document actual = this.sut.canReceiveMethodResultUsingPermissionEvaluator("asd555");
        Document expecting = new Document("green");
        assertEquals(actual, expecting);
    }

    @Test
    @WithMockUser(username = "blue", roles = "admin")
    void testWithAdminRole() {
        Document actual = this.sut.canReceiveMethodResultUsingPermissionEvaluator("asd555");
        Document expecting = new Document("green");
        assertEquals(actual, expecting);
    }

}
