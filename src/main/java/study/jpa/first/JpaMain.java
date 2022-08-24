package study.jpa.first;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.transaction.Transaction;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
//        save();
//        find();
        findByJpql();
    }

    private static void save() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-study");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Member member = new Member();
            member.setId(2L);
            member.setName("test2");
            entityManager.persist(member);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }

    private static void find() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-study");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            Member member = entityManager.find(Member.class, 1L);
            member.setName("test11");
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }
    private static void findByJpql() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("jpa-study");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        try {
            List<Member> members = entityManager.createQuery("select m from Member as m")
                    .setFirstResult(0)
                    .setMaxResults(8)
                    .getResultList();

            for (Member member : members) {
                System.out.println("member.name = "+member.getName());
            }
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            entityManager.close();
        }
    }
}
