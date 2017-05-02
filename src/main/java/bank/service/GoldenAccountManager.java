package bank.service;

import bank.Models.Account;
import bank.Repository.AccountRepository;
import bank.Repository.AccountJavaRepository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GoldenAccountManager {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");

    public Account createGoldenAccount(Long accountNr) {
        EntityManager em = emf.createEntityManager();
        AccountRepository accountRepository = new AccountJavaRepository(em);
        Account account = new Account(accountNr);
        account.setThreshold(-1000L);
        em.getTransaction().begin();
        try {
            accountRepository.create(account);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return account;
    }

    public Account updrade2GoldenAccount(Long accountNr) {
        EntityManager em = emf.createEntityManager();
        AccountRepository accountRepository = new AccountJavaRepository(em);
        Account account = null;
        em.getTransaction().begin();
        try {
            account = accountRepository.findByAccountNr(accountNr);
            account.setThreshold(-1000L);
            accountRepository.edit(account);

            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
        } finally {
            em.close();
        }

        return account;
    }
}
