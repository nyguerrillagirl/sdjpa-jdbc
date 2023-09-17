package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.AuthorDao;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("local")
@DataJpaTest
@ComponentScan(basePackages = {"guru.springframework.jdbc.dao"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoIntegrationTest {

    @Autowired
    AuthorDao authorDao;

    @Test
    void testGetAuthor() {
        Author author = authorDao.getById(1L);

        assertThat(author).isNotNull();
    }

    @Test
    void testGetAuthorByName() {
        Author fetchedAuthor = authorDao.findAuthorByName("Eric", "Evans");
        assertThat(fetchedAuthor).isNotNull();
        Assertions.assertEquals("Eric", fetchedAuthor.getFirstName());
        Assertions.assertEquals("Evans", fetchedAuthor.getLastName());
    }

    @Test
    void testSaveNewAuthor() {
        Author newAuthor = new Author("Lorraine", "Figueroa");
        Author savedAuthor = authorDao.saveNewAuthor(newAuthor);

        assertThat(savedAuthor).isNotNull();
    }

    @Test
    void testUpdateAuthor() {
        // First create a new author
        Author author = new Author("Samantha", "Neill");
        Author savedAuthor = authorDao.saveNewAuthor(author);
        // After saving, update to a different last name
        savedAuthor.setLastName("O'Neill");
        Author updatedAuthor = authorDao.updateAuthor(savedAuthor);

        assertThat(updatedAuthor.getLastName()).isEqualTo("O'Neill");
    }
}
