package bank.Models;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;


public class AccountTest {

    private static EntityManager entityManager;

    @BeforeClass
    public static void CreateEntityManager(){
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("bankPU");
        entityManager = entityManagerFactory.createEntityManager();
    }

    @Test
    public void TestOpdrOne(){
        Account account = new Account(111L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        //TODO: verklaar en pas eventueel aan
        assertNull(account.getId());
        entityManager.getTransaction().commit();
        System.out.println("AccountId:"  + account.getId());
        //TODO: verklaar en pas eventueel aan
        assertTrue(account.getId() > 0L);

    }

}