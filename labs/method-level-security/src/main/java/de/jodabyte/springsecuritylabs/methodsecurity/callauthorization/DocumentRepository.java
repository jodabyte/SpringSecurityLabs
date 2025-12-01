package de.jodabyte.springsecuritylabs.methodsecurity.callauthorization;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class DocumentRepository {

    private Map<String, Document> documents =
            Map.of("abc123", new Document("blue"),
                    "qwe123", new Document("blue"),
                    "asd555", new Document("green"));


    public Document findDocument(String code) {
        return documents.get(code);
    }
}
