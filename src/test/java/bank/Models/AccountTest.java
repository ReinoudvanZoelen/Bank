package bank.Models;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.*;


public class AccountTest {

    private static EntityManager entityManager;
    private static EntityManagerFactory entityManagerFactory;

    @BeforeClass
    public static void CreateEntityManager(){
        entityManagerFactory = Persistence.createEntityManagerFactory("bankPU");
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

    @Test
    public void TestOpdrTwo(){
        Account account = new Account(111L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        assertNull(account.getId());
        entityManager.getTransaction().rollback();
        // TODO code om te testen dat table account geen records bevat. Hint: bestudeer/gebruik AccountDAOJPAImpl
    }

    @Test
    public void TestOpdrThree(){

        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        //TODO: verklaar en pas eventueel aan
        //assertNotEquals(expected, account.getId();
        entityManager.flush();
        //TODO: verklaar en pas eventueel aan
        //assertEquals(expected, account.getId();
        entityManager.getTransaction().commit();
        //TODO: verklaar en pas eventueel aan

    }

    @Test
    public void TestOpdrFour(){

        Long expectedBalance = 400L;
        Account account = new Account(114L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        account.setBalance(expectedBalance);
        entityManager.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());
//TODO: verklaar de waarde van account.getBalance
        Long  cid = account.getId();
        account = null;
        EntityManager em2 = entityManagerFactory.createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class,  cid);
//TODO: verklaar de waarde van found.getBalance
        assertEquals(expectedBalance, found.getBalance());

    }

    @Test
    public void TestOpdrFive(){

    }

    @Test
    public void TestOpdrSix(){


        Account acc = new Account(1L);
        Account acc2 = new Account(2L);
        Account acc9 = new Account(9L);

// scenario 1
        Long balance1 = 100L;
        entityManager.getTransaction().begin();
        entityManager.persist(acc);
        acc.setBalance(balance1);
        entityManager.getTransaction().commit();
//TODO: voeg asserties toe om je verwachte waarde van de attributen te verifieren.
//TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.


// scenario 2
        Long balance2a = 211L;
        acc = new Account(2L);
        entityManager.getTransaction().begin();
        acc9 = entityManager.merge(acc);
        acc.setBalance(balance2a);
        acc9.setBalance(balance2a+balance2a);
        entityManager.getTransaction().commit();
//TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
//TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
// HINT: gebruik acccountDAO.findByAccountNr


// scenario 3
        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        entityManager.getTransaction().begin();
        Account acc2a = entityManager.merge(acc);
        assertTrue(entityManager.contains(acc)); // verklaar
        assertTrue(entityManager.contains(acc2)); // verklaar
        assertEquals(acc,acc2a);  //verklaar
        acc2.setBalance(balance3b);
        acc.setBalance(balance3c);
        entityManager.getTransaction().commit() ;
//TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
//TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.


// scenario 4
        Account account = new Account(114L) ;
        account.setBalance(450L) ;
        EntityManager em = entityManagerFactory.createEntityManager() ;
        em.getTransaction().begin() ;
        em.persist(account) ;
        em.getTransaction().commit() ;

        Account account2 = new Account(114L) ;
        Account tweedeAccountObject = account2 ;
        tweedeAccountObject.setBalance(650l) ;
        assertEquals((Long)650L,account2.getBalance()) ;  //verklaar
        account2.setId(account.getId()) ;
        em.getTransaction().begin() ;
        account2 = em.merge(account2) ;
        assertSame(account,account2) ;  //verklaar
        assertTrue(em.contains(account2)) ;  //verklaar
        assertFalse(em.contains(tweedeAccountObject)) ;  //verklaar
        tweedeAccountObject.setBalance(850l) ;
        assertEquals((Long)650L,account.getBalance()) ;  //verklaar
        assertEquals((Long)650L,account2.getBalance()) ;  //verklaar
        em.getTransaction().commit() ;
        em.close() ;

    }

    @Test
    public void TestOpdrSeven(){

        Account acc1 = new Account(77L);
        entityManager.getTransaction().begin();
        entityManager.persist(acc1);
        entityManager.getTransaction().commit();
//Database bevat nu een account.

// scenario 1
        Account accF1;
        Account accF2;
        accF1 = entityManager.find(Account.class, acc1.getId());
        accF2 = entityManager.find(Account.class, acc1.getId());
        assertSame(accF1, accF2);

// scenario 2
        accF1 = entityManager.find(Account.class, acc1.getId());
        entityManager.clear();
        accF2 = entityManager.find(Account.class, acc1.getId());
        assertSame(accF1, accF2);
//TODO verklaar verschil tussen beide scenario’s

    }

    @Test
    public void TestOpdrEight(){

        Account acc1 = new Account(88L);
        entityManager.getTransaction().begin();
        entityManager.persist(acc1);
        entityManager.getTransaction().commit();
        Long id = acc1.getId();
//Database bevat nu een account.

        entityManager.remove(acc1);
        assertEquals(id, acc1.getId());
        Account accFound = entityManager.find(Account.class, id);
        assertNull(accFound);
//TODO: verklaar bovenstaande asserts

    }

    @Test
    public void TestOpdrNine(){

    }

}