package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.domain.Author;
import guru.springframework.jdbc.domain.Book;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {

    @Autowired
    BookDao bookDao;

    @Autowired
    AuthorDao authorDao;

    private void cleanUpBook(Book book) {
        try {
            bookDao.deleteBookById(book.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    void testGetBook() {
        Book fetchedBook = bookDao.getById(1L);
        assertThat(fetchedBook).isNotNull();
    }
    @Test
    void testGetBookByTitle() {
        Book fetchedBook = bookDao.findBookByTitle("Domain-Driven Design");
        assertThat(fetchedBook).isNotNull();
        Assertions.assertEquals("978-0321125217", fetchedBook.getIsbn());
        Assertions.assertEquals("Addison Wesley", fetchedBook.getPublisher());
    }
    @Test
    void testSaveNewBook() {
        Book newBook = new Book("978-1-83921-662-6", "Packt Publishing", "The C++ Workshop");
        Book savedBook = bookDao.saveNewBook(newBook);

        assertThat(savedBook).isNotNull();
        Assertions.assertEquals("978-1-83921-662-6", savedBook.getIsbn());

        cleanUpBook(savedBook);
    }

    @Test
    void testSaveNewBook2() {
        Book newBook = new Book("978-1-83921-662-6", "Packt Publishing", "The C++ Workshop");
        Author author = authorDao.getById(1L);
        newBook.setAuthor(author);

        Book savedBook = bookDao.saveNewBook(newBook);

        assertThat(savedBook).isNotNull();
        Assertions.assertEquals("978-1-83921-662-6", savedBook.getIsbn());

        cleanUpBook(savedBook);
    }

    @Test
    void testUpdateBook() {
        // First create a new author
        Book newBook = new Book("978-1-83921-662-6", "Packt Publishing", "The C++ Workshop");
        Book savedBook = bookDao.saveNewBook(newBook);

        // After saving, set the author
        Author bookAuthor = authorDao.getById(1L);
        savedBook.setAuthor(bookAuthor);

        Book updatedBook = bookDao.updateBook(savedBook);

        assertThat(updatedBook.getAuthor().getId()).isEqualTo(1L);
        cleanUpBook(updatedBook);
    }
}
