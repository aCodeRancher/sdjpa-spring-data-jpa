package guru.springframework.jdbc;

import guru.springframework.jdbc.dao.BookDao;
import guru.springframework.jdbc.dao.BookDaoImpl;
import guru.springframework.jdbc.domain.Book;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@DataJpaTest
@Import({BookDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookDaoIntegrationTest {

      @Autowired
      BookDao bookDao;

     @Test
     void testDeleteBook() {
         Book book = new Book();
         book.setIsbn("1234");
         book.setPublisher("Self");
         book.setTitle("my book");
         Book saved = bookDao.saveNewBook(book);

          bookDao.deleteBookById(saved.getId());

         Assertions.assertThrows(EntityNotFoundException.class, ()->bookDao.getById(saved.getId()));

     }

     @Test
     void updateBookTest() {
         Book book = new Book();
         book.setIsbn("1234");
         book.setPublisher("Self");
         book.setTitle("my book");

         Book saved = bookDao.saveNewBook(book);

         saved.setTitle("New Book");
         bookDao.updateBook(saved);

         Book fetched = bookDao.getById(saved.getId());

         assertThat(fetched.getTitle()).isEqualTo("New Book");

         bookDao.deleteBookById(fetched.getId());
     }

    @Test
     void testSaveBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");

        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();

        bookDao.deleteBookById(saved.getId());
    }

    @Test
    void testGetBookByTitle() {
         Book book = bookDao.findBookByTitle("Clean Code");

         assertThat(book).isNotNull();
     }

     @Test
     void testGetBook() {
         Book book = bookDao.getById(3L);

         assertThat(book.getId()).isNotNull();
    }

}
