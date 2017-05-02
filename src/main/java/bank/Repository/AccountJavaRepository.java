package bank.Repository;

import bank.Models.Account;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class AccountJavaRepository implements AccountRepository {

    private final EntityManager em;

    public AccountJavaRepository(EntityManager em) {
        this.em = em;
    }

    public void create(Account account) {
        em.persist(account);
    }

    public void edit(Account account) {
        em.merge(account);
    }

    public void remove(Account account) {
        em.remove(em.merge(account));
    }

    public Account find(Long id) {
        return em.find(Account.class, id);
    }

    public Account findByAccountNr(Long accountNr) {
        Query q = em.createNamedQuery("Account.findByAccountNr", Account.class);
        q.setParameter("accountNr", accountNr);
        return (Account) q.getSingleResult();
    }

    public List<Account> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Account.class));
        return em.createQuery(cq).getResultList();
    }

    public int count() {
        Query q = em.createNamedQuery("Account.count", Account.class);
        return ((Long) q.getSingleResult()).intValue();
    }
}
