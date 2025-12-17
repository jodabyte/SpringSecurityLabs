package de.jodabyte.springsecuritylabs.methodsecurity.callauthorization;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AccessService {

    public final static String ACCESS_GRANTED = "Access granted";
    private final DocumentRepository repository;
    private final Map<String, Employee> records =
            Map.of("blue",
                    new Employee("Blue",
                            List.of("Karamazov Brothers"),
                            List.of("accountant", "reader")),
                    "green",
                    new Employee("Green",
                            List.of("Beautiful Paris"),
                            List.of("researcher"))
            );

    public AccessService(DocumentRepository repository) {
        this.repository = repository;
    }

    @PreAuthorize("hasAuthority('write')")
    public String canCallWithWriteAuthority() {
        return ACCESS_GRANTED;
    }

    @PreAuthorize("#name == authentication.principal.username")
    public String canCallWithCorrectUsername(String name) {
        return ACCESS_GRANTED;
    }

    @PostAuthorize("returnObject.roles.contains('reader')")
    public Employee canReceiveMethodResultWithCorrectRole(String name) {
        return this.records.get(name);
    }

    @PostAuthorize("hasPermission(returnObject, 'ROLE_admin')")
    public Document canReceiveMethodResultUsingPermissionEvaluator(String code) {
        return this.repository.findDocument(code);
    }
}
