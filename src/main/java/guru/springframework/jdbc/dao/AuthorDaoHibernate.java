package guru.springframework.jdbc.dao;

import guru.springframework.jdbc.domain.Author;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class AuthorDaoHibernate implements AuthorDao {

    private final EntityManagerFactory emf;

    public AuthorDaoHibernate(EntityManagerFactory emf) {
            this.emf = emf;
     }

        @Override
        public List<Author> findAllAuthorsByLastName(String lastname, Pageable pageable ) {
           StringBuilder sb = new StringBuilder();
           sb.append("SELECT a FROM Author a WHERE a.lastName =  :lastname") ;
           EntityManager em = getEntityManager();
           try {
               if (pageable.getSort().getOrderFor("first_name") != null){
                   sb.append(" order by a.firstName ").append(pageable.getSort().getOrderFor("first_name").getDirection().name());
                }

               TypedQuery<Author> query = em.createQuery(sb.toString(),Author.class);
               query.setParameter("lastname", lastname);
               query.setFirstResult(Math.toIntExact(pageable.getOffset()));
               query.setMaxResults(pageable.getPageSize());
               return query.getResultList();
           }
           finally{
               em.close();
           }
        }



       public Author getById(Long id){
           return null;
       }

       public Author findAuthorByName(String firstName, String lastName){
           return null;
       }

        public  Author saveNewAuthor(Author author){
           return null;
        }

        public Author updateAuthor(Author author){
           return null;
        }

        public void deleteAuthorById(Long id){

         }


        private EntityManager getEntityManager(){
            return emf.createEntityManager();
        }
}
