package study.jpa.second;

import study.jpa.domain.Book;
import study.jpa.domain.Member;
import study.jpa.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaSecondMain {
    public static void main(String[] args) {
        save();
    }

    private static void save() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-study");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Book book = new Book();
            book.setName("JPA");
            book.setAuthor("κΉμν");
            entityManager.persist(book);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }
}
