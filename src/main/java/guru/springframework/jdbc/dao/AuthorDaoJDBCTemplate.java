package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;


public class AuthorDaoJDBCTemplate implements AuthorDao{

    private JdbcTemplate jdbcTemplate;

    public AuthorDaoJDBCTemplate(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAllAuthorByLastName(Pageable pageable) {
        String sql = "SELECT * FROM author order by first_name " + pageable
                .getSort().getOrderFor("first_name").getDirection().name()
                + " limit ? offset ?";

        System.out.println(sql);

        return jdbcTemplate.query(sql, getAuthorMapper(), pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Author> findAllAuthors(Pageable pageable) {
        return jdbcTemplate.query("SELECT * FROM author limit ? offset ?", getAuthorMapper(), pageable.getPageSize(),
                pageable.getOffset());
    }
    @Override
    public Author getById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM author where id = ?", getAuthorMapper(), id);
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject("SELECT * FROM author where first_name = ? and last_name= ?",
                 getAuthorMapper(), firstName, lastName );
    }

    @Override
    public Author saveNewAuthor(Author author) {
        jdbcTemplate.update("INSERT INTO author (first_name, last_name) VALUES (?, ?)",
                author.getFirstName(), author.getLastName());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update("UPDATE author set first_name = ?, last_name = ? where id = ?",
                   author.getFirstName(), author.getLastName(), author.getId());

         return this.getById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
            jdbcTemplate.update("DELETE from author where id = ?", id);
    }

    private AuthorMapper getAuthorMapper(){
        return new AuthorMapper();
    }
}
