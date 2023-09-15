package guru.springframework.jdbc;

import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.AuthorRepository;
import guru.springframework.jdbc.repositories.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MySQLIntegrationTest {
    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    @Test
    public void testMySql() {
        long countBefore = bookRepository.count();
        assertThat(countBefore).isGreaterThan(0);
    }

    @Test
    public void testAddingBook() {
        Book newBook = new Book("1234567890", "Wiley", "Ultimate Guide to Atari 2600 Programming");
        Book savedBook = bookRepository.save(newBook);
        assertNotNull(savedBook);
        // Retreive the book
        Book fetchedBook = bookRepository.getReferenceById(savedBook.getId());
        assertThat(fetchedBook.getTitle()).isEqualTo(newBook.getTitle());
    }
    @Test
    public void testAddingAuthor() {
        Author newAuthor = new Author("Lorraine", "Figueroa");
        Author savedAuthor = authorRepository.save(newAuthor);
        assertNotNull(savedAuthor);
        // Retreive the author
        Author fetchedAuthor = authorRepository.getReferenceById(savedAuthor.getId());
        assertThat(fetchedAuthor.getFirstName()).isEqualTo(newAuthor.getFirstName());
    }
}
