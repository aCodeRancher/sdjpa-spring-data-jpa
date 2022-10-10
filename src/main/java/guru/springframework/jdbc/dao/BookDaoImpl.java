package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Book;
import guru.springframework.jdbc.repositories.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class BookDaoImpl implements BookDao{

    private BookRepository bookRepository;

    public BookDaoImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    public Book getById(Long id){
      return   bookRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

   public  Book findBookByTitle(String title){
       return  bookRepository.findBookByTitle(title).orElseThrow(EntityNotFoundException::new);
   }

   public Book saveNewBook(Book book){
        return bookRepository.save(book);
   }

   @Transactional
   public Book updateBook(Book book){
      Book fetched =   bookRepository.findById(book.getId()).orElseThrow(EntityNotFoundException::new);
      fetched.setTitle(book.getTitle());
      fetched.setPublisher(book.getPublisher());
      fetched.setAuthorId(book.getAuthorId());
      fetched.setIsbn(book.getIsbn());
      return bookRepository.save(fetched);
   }

   public void deleteBookById(Long id){
        bookRepository.deleteById(id);
   }

}
