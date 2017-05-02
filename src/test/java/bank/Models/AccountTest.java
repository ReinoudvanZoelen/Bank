package bank.Models;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.DatabaseCleaner;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import static org.junit.Assert.*;



public class AccountTest {
    
    private static EntityManager entityManager;

    @Before
    public void CreateEntityManager() {
        entityManager = Persistence.createEntityManagerFactory("bankPU").createEntityManager();
    }

    @After
    public void CleanDatabase() {
        try {
            new DatabaseCleaner(entityManager).clean();
        } catch (Exception ex) {
            System.out.println("The database was not cleaned. Error: " + ex);
        }
    }

    // 1.
    // 2.
    // 3.
    // 4.

    //1.	Wat is de waarde van asserties en printstatements? Corrigeer verkeerde asserties zodat de test ‘groen’ wordt.
    //2.	Welke SQL statements worden gegenereerd?
    //3.	Wat is het eindresultaat in de database?
    //4.	Verklaring van bovenstaande drie observaties.

    @Test
    public void TestOpdrOne() {
        Account account = new Account(111L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        //TODO: verklaar en pas eventueel aan
        // 1. De entitiymanager wordt verteld om de account bij te houden
        // 2. Er is nog geen sql statement gemaakt
        // 3. De database is onveranderd
        // 4. Een persist houdt een staat bij maar doet er nog niets mee
        assertNull(account.getId());
        entityManager.getTransaction().commit();
        System.out.println("AccountId:" + account.getId());
        //TODO: verklaar en pas eventueel aan
        // 1. AssertNull geeft true want het object heeft nog geen ID van de
        // 2. insert into Account (...) values (...)
        // 3. De account is toegevoegd aan de database en heeft een ID gekregen
        // 4. Bij de .commit() worden de openstaande persists toegevoegd
        assertTrue(account.getId() > 0L);
    }


    @Test
    public void TestOpdrTwo() {
        Account account = new Account(111L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        assertNull(account.getId());
        entityManager.getTransaction().rollback();
        // TODO code om te testen dat table account geen records bevat. Hint: bestudeer/gebruik AccountDAOJPAImpl
        // 1. Assert returnt true. Account heeft nog geen ID omdat deze niet in de database is gezet
        // 2. Er wordt geen SQL gegenereerd omdat .commit niet is gebruikt
        // 3. Er zijn geen wijzigingen aangebracht
        // 4. .rollback wordt gebruikt dus elke transactie vanaf .begin wordt niet uigevoerd
    }

    @Test
    public void TestOpdrThree() {
        Long expected = -100L;
        Account account = new Account(111L);
        account.setId(expected);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        //TODO: verklaar en pas eventueel aan
        // 1. Account wordt bijgehouden in de transaction
        // 2. insert into (...) values (...)
        // 3. Er is nog niet gecommit, er zijn geen aanpasssingen gemaakt
        // 4. Account wordt klaar gezet om te worden gecommit
        assertEquals(expected, account.getId());
        entityManager.flush();
        //TODO: verklaar en pas eventueel aan
        // 1. Alle entiteiten worden nu direct opgeslagen
        // 2. insert into Account (...) values (...)
        // 3. De account is in de database opgeslagen
        // 4. Flush voert een directe save uit naar de database
        assertNotEquals(expected, account.getId());
        entityManager.getTransaction().commit();
        //TODO: verklaar en pas eventueel aan
        // 1. Er zijn geen wijzigingen dus commit update nog een keer exact het zelfde
        // 2. Geen SQL, er zijn geen wijzigingen
        // 3. Niets
        // 4. We hebben een ID gegeven van -100, deze wordt door de database overschreven
    }

    @Test
    public void TestOpdrFour() {
        Long expectedBalance = 400L;
        Account account = new Account(114L);
        entityManager.getTransaction().begin();
        entityManager.persist(account);
        account.setBalance(expectedBalance);
        entityManager.getTransaction().commit();
        assertEquals(expectedBalance, account.getBalance());
        //TODO: verklaar de waarde van account.getBalance
        // 1. True, de expectedbalance
        // 2. Insert into Account (...) values (...)
        // 3. De account staat in de database met een saldo van 400
        // 4. Account wordt aangemaakt, bijgehouden en in de database gezet
        //    Persist wordt nog niet uitgevoerd en daarom wordt de actie van .setBalance meegenomen
        Long cid = account.getId();
        account = null;
        EntityManager em2 = Persistence.createEntityManagerFactory("bankPU").createEntityManager();
        em2.getTransaction().begin();
        Account found = em2.find(Account.class, cid);
        //TODO: verklaar de waarde van found.getBalance
        // 1. True, hetzelfde object is opgehaald
        // 2. Get * from Account where id = cid
        // 3. Niets, er wordt alleen data opgehaald
        // 4. Hetzelfde object wordt uit de database opgehaald, al via een andere omweg
        assertEquals(expectedBalance, found.getBalance());

    }

    @Test
    public void TestOpdrFive() {
        //In de vorige opdracht verwijzen de objecten account en found naar dezelfde rij in de database.
        //Pas een van de objecten aan, persisteer naar de database. Refresh vervolgens het andere object
        //om de veranderde state uit de database te halen. Test met asserties dat dit gelukt is.

        Long expectedBalance = 400L;

        Account account = new Account(114l);
        account.setId(6l);
        account.setBalance(expectedBalance);

        entityManager.getTransaction().begin();
        entityManager.persist(account);
        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();

        //Long id = 1l;

        Account a1 = (Account) entityManager.find(Account.class, account.getId());
        assertEquals(expectedBalance, a1.getBalance());
        a1.setBalance(a1.getBalance() - 1);
        assertNotEquals(expectedBalance, a1.getBalance());

        Account a2 = (Account) entityManager.find(Account.class, account.getId());
        assertTrue(a1 == a2);

        entityManager.refresh(a1);
        assertTrue(a1 == a2);
        assertEquals(expectedBalance, a1.getBalance());
        assertEquals(expectedBalance, a2.getBalance());

        entityManager.getTransaction().commit();
    }

    @Test
    public void TestOpdrSixScen1() {
        Account acc = new Account(1L);

        Long balance1 = 100L;
        entityManager.getTransaction().begin();
        entityManager.persist(acc);
        acc.setBalance(balance1);
        entityManager.getTransaction().commit();
        assertEquals(balance1, acc.getBalance());
        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifieren.
        // 1. True, de balance wordt opgeslagen
        // 2. insert into Account (...) values (...)
        // 3. Er staat een account in de database
        // 4. Al eerder uitgelegd bovenstaand

        Account account2 = entityManager.find(Account.class, acc.getId());
        entityManager.getTransaction().begin();
        entityManager.persist(account2);
        acc.setBalance((balance1 * 2));
        entityManager.getTransaction().commit();
        assertEquals(account2.getBalance(), acc.getBalance());
        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
    }

    @Test
    public void TestOpdrSixScen2() {

        Account account = new Account(1L);
        Account accountCheck = new Account(1L);

        Long balance2a = 211L;
        account = new Account(2L);

        entityManager.getTransaction().begin();
        accountCheck = entityManager.merge(account);
        assertEquals(accountCheck.getBalance(), account.getBalance());
        account.setBalance(balance2a);
        accountCheck.setBalance(balance2a + balance2a);
        assertNotEquals(accountCheck.getBalance(), account.getBalance());
        assertNotNull(accountCheck.getBalance());
        entityManager.getTransaction().commit();
        //TODO: voeg asserties toe om je verwachte waarde van de attributen te verifiëren.
        //TODO: doe dit zowel voor de bovenstaande java objecten als voor opnieuw bij de entitymanager opgevraagde objecten met overeenkomstig Id.
        // HINT: gebruik acccountDAO.findByAccountNr
        // 1. De asserts zijn geplaatst op plaatsen waar de waardes veranderen of veranderd zijn
        // 2. niets nieuws
        // 3. Account en AccountCheck zijn aangemaakt en accountcheck heeft dubbel de balans van
        // 4. Merge wordt gebruikt om een kopie te maken die niet actief wordt bijgehouden
    }

    @Test
    public void TestOpdrSixScen3() {
        Account acc = new Account(1L);
        Account acc2 = new Account(1L);
        Account accountCheck = new Account(1L);

        Long balance3b = 322L;
        Long balance3c = 333L;
        acc = new Account(3L);
        entityManager.getTransaction().begin();
        Account acc2a = entityManager.merge(acc);
        assertFalse(entityManager.contains(acc)); // verklaar
        assertFalse(entityManager.contains(acc2)); // verklaar
        // De reden dat deze false zijn is omdat de accounts niet in de entitymanager gezet zijn
        // Merge haalt alleen iets op, houdt ze niet bij
        assertEquals(acc, acc2a);  //verklaar
        // acc en acc2a zijn het zelfde omdat acc2a uit acc wordt gekopiëerd
        acc2.setBalance(balance3b);
        acc.setBalance(balance3c);
        entityManager.getTransaction().commit();
    }

    @Test
    public void TestOpdrSixScen4() {

        Account acc = new Account(1L);
        Account acc2 = new Account(1L);
        Account accountCheck = new Account(1L);

        Account account = new Account(114L);
        account.setBalance(450L);
        EntityManager em = Persistence.createEntityManagerFactory("bankPU").createEntityManager();
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();

        Account account2 = new Account(114L);
        Account tweedeAccountObject = account2;
        tweedeAccountObject.setBalance(650l);
        assertEquals((Long) 650L, account2.getBalance());  //verklaar
        // Er wordt een kopie gemaakt van de referentie, beiden objecten gaan naar hetzelfde geheugen adres
        account2.setId(account.getId());
        em.getTransaction().begin();
        account2 = em.merge(account2);
        assertSame(account, account2);  //verklaar
        // De objecten worden vergeleken en de geheugenadressen zijn hetzelfde
        assertTrue(em.contains(account2));  //verklaar
        // omdat em.merge(account2) wordt gebruikt staat account2 in de EntityManager
        assertFalse(em.contains(tweedeAccountObject));  //verklaar
        // omdat tweedeAccountObject niet wordt gebruikt door de EntityManager wordt deze ook niet bijgehouden
        tweedeAccountObject.setBalance(850l);
        assertEquals((Long) 650L, account.getBalance());  //verklaar
        assertEquals((Long) 650L, account2.getBalance());  //verklaar
        // account en account2 hebben het zelfde geheugenadres
        em.getTransaction().commit();
        em.close();
    }


    @Test
    public void TestOpdrSeven() {

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
        //Scenario 1 gaan allebei naar hetzelfde geheugen address
        //Scenario 2 is de cashe van de entityManager gecleared en krijgt accF2 een nieuw geheugen address
    }

    @Test
    public void TestOpdrEight() {

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

        //assertEquals  het ID en de acc1.getID staan hier nog in het geheugen maar niet meer in de db.
        //assertNull er staan op dit moment geen accounts meer in de db
    }

    @Test
    public void TestOpdrNine() {
        // Een identity heeft een aparte ID per Type
        // Een sequence heeft één ID die doortelt voor elk object (vóór de commit)
        // Sequence houdt alleen het laatste nummer bij en "rolt door"
        // Een table onthoudt meer dan alleen het laatste nummer
        // Table vraagt iedere keer uit een tabel op wat het laatste nummer moet zijn
    }

}