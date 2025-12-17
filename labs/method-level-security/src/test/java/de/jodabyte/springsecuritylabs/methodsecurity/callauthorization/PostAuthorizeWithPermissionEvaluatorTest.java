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
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When no user is authenticated
            Then throw an AuthenticationException.
            """)
    void testWithNoUser() {
        assertThrows(AuthenticationException.class,
                () -> this.sut.canReceiveMethodResultUsingPermissionEvaluator("abc123"));
    }

    @Test
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When user permission does not match the rules
            Then throw an AuthorizationDeniedException.
            """)
    @WithMockUser(username = "green", roles = "manager")
    void testWithManagerRole() {
        assertThrows(AuthorizationDeniedException.class,
                () -> this.sut.canReceiveMethodResultUsingPermissionEvaluator("abc123"));
    }

    @Test
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When user permission does match the rules
            Then return the object.
            """)
    @WithMockUser(username = "green", roles = "manager")
    void testWithManagerRoleForOwnUserDocument() {
        Document actual = this.sut.canReceiveMethodResultUsingPermissionEvaluator("asd555");
        Document expecting = new Document("green");
        assertEquals(actual, expecting);
    }

    @Test
    @DisplayName("""
            Given the return object of a method is protected by a authorization rule
            When user permission does not match the rules but has admin role
            Then return the object.
            """)
    @WithMockUser(username = "blue", roles = "admin")
    void testWithAdminRole() {
        Document actual = this.sut.canReceiveMethodResultUsingPermissionEvaluator("asd555");
        Document expecting = new Document("green");
        assertEquals(actual, expecting);
    }

}
