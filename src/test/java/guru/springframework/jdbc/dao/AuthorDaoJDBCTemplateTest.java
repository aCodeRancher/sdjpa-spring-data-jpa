package guru.springframework.jdbc.dao;

import com.google.common.collect.Ordering;
import guru.springframework.jdbc.domain.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("local")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorDaoJDBCTemplateTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    AuthorDao authorDao;

    @BeforeEach
    void setUp() {
        authorDao = new AuthorDaoJDBCTemplate(jdbcTemplate);
    }

    @Test
    void findAllAuthorPage1SortByTitle() {
        List<Author> authors = authorDao.findAllAuthorByLastName(PageRequest.of(0, 10,
                Sort.by(Sort.Order.desc("first_name"))));

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isGreaterThanOrEqualTo(10);
        List<String> authorFN= authors.stream().map(Author::getFirstName).collect(Collectors.toList());
        assertTrue(Ordering.natural().reverse().isOrdered(authorFN));
        assertTrue(authorFN.get(0).equals("Yugal"));
        assertTrue(authorFN.get(9).equals("Tishara"));

    }

    @Test
    void findAllAuthorPage2SortByTitleDesc() {
        List<Author> authors = authorDao.findAllAuthorByLastName(PageRequest.of(1, 10,
                Sort.by(Sort.Order.desc("first_name"))));

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isGreaterThanOrEqualTo(10);
        List<String> authorFN= authors.stream().map(Author::getFirstName).collect(Collectors.toList());
        assertTrue(Ordering.natural().reverse().isOrdered(authorFN));
        assertTrue(authorFN.get(0).equals("Surendra"));
        assertTrue(authorFN.get(9).equals("Pooja"));
    }

    @Test
    void findAllAuthorPage3SortByTitleAsc() {
        List<Author> authors = authorDao.findAllAuthorByLastName(PageRequest.of(2, 10,
                Sort.by(Sort.Order.asc("first_name"))));

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isGreaterThanOrEqualTo(10);
        List<String> authorFN= authors.stream().map(Author::getFirstName).collect(Collectors.toList());
        assertTrue(Ordering.natural().isOrdered(authorFN));
        assertTrue(authorFN.get(0).equals("Mekandor"));
        assertTrue(authorFN.get(9).equals("Serhad"));
    }


    @Test
    void findAllAuthorsPage1() {
        List<Author> authors = authorDao.findAllAuthors(PageRequest.of(0, 10));

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isEqualTo(10);
    }

    @Test
    void findAllAuthorsPage2() {
        List<Author> authors = authorDao.findAllAuthors(PageRequest.of(1, 10));

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isEqualTo(10);
    }



    @Test
    void getById() {
        Author author = authorDao.getById(3L);

        assertThat(author.getId()).isNotNull();
    }

    @Test
    void findBookByTitle() {
        Author author = authorDao.findAuthorByName("Patrick", "Smith");

        assertThat(author).isNotNull();
    }

    @Test
    void saveNewBook() {
        Author author = new Author();
        author.setFirstName("Jane");
        author.setLastName("Doe");
        Author saved = authorDao.saveNewAuthor(author);
        assertThat(saved).isNotNull();
        authorDao.deleteAuthorById(saved.getId());
    }

    @Test
    void updateAuthor() {
       Author author = new Author();
       author.setFirstName("Peter");
       author.setLastName("Pan");

       Author saved = authorDao.saveNewAuthor(author);
       saved.setLastName("Pang");
       authorDao.updateAuthor(saved);

       Author fetched = authorDao.getById(saved.getId());

        assertThat(fetched.getLastName()).isEqualTo("Pang");
    }

    @Test
    void deleteAuthorById() {

        Author author = new Author();
        author.setLastName("Schor");
        author.setFirstName("Nate");

        Author saved = authorDao.saveNewAuthor(author);

        authorDao.deleteAuthorById(saved.getId());

        assertThrows(EmptyResultDataAccessException.class, () -> {
            authorDao.getById(saved.getId());
        });
    }
}
