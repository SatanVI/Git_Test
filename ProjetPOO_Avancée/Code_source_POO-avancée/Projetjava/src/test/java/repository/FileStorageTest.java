package repository;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import domain.Book;

public class FileStorageTest {

    @Test
    public void saveAndLoadBooks_roundtrip() throws Exception {
        FileStorage fs = FileStorage.getInstance();
        Path booksPath = Paths.get("data", "books.txt");

        List<String> backup = null;
        if (Files.exists(booksPath)) {
            backup = Files.readAllLines(booksPath, StandardCharsets.UTF_8);
        }

        try {
            Book b = new Book(
                java.util.UUID.randomUUID().toString(),
                "ISBN-TEST-123",
                "TitreTest",
                "AuteurTest",
                null,
                9.99,
                7
            );

            fs.saveBooks(List.of(b));
            Map<String, Book> loaded = fs.loadBooks();

            assertNotNull(loaded);
            assertTrue(loaded.containsKey("ISBN-TEST-123"));

            Book lb = loaded.get("ISBN-TEST-123");
            assertEquals("TitreTest", lb.getTitre());
            assertEquals("AuteurTest", lb.getAuteur());
            assertEquals(9.99, lb.getPrix());
            assertEquals(7, lb.getStock());

        } finally {
            if (backup != null) {
                Files.write(booksPath, backup, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                Files.write(booksPath, List.of(), StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }
}