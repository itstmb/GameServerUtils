package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.CCharacter;
import com.gameserver.utils.account.service.AccountServiceImpl;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AccountDaoImpl() {
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public Integer save(Account account) throws RuntimeException {
        LOGGER.debug("Saving account [{}]", account.getUsername());
        Session session = getSession();
        try {
            int accountId = (int) session.save(account);
            LOGGER.info("Account [{}] ([{}]) saved successfully", account.getUsername(), accountId);
            return accountId;
        } catch (Exception e) {
            LOGGER.error("Could not save account [{}] due to exception: {}", account.getUsername(), e);
            throw new RuntimeException("Account save failed");
        }
    }

    @Override
    public void deleteById(int id) throws RuntimeException {
        LOGGER.debug("Deleting account: [{}]", id);
        Session session = getSession();
        var query = session.createQuery("delete from Account where id=:id");
        query.setParameter("id", id);
        try {
            query.executeUpdate();
            LOGGER.info("Account with id [{}] deleted successfully", id);
        } catch (Exception e) {
            LOGGER.error("Could not delete account with id [{}] due to exception: {}", id, e);
            throw new RuntimeException("Account deletion failed");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Account getById(int id) {
        LOGGER.debug("Searching account by id: [{}]", id);
        Session session = getSession();

        String hql = "from Account a where a.id = :id";
        List<Account> result = session.createQuery(hql)
                .setParameter("id", id)
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Account with id [{}] cannot be found", id);
            return null;
        }
        LOGGER.debug("Account with id [{}] found, username: [{}]", id, result.get(0).getUsername());
        return result.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Account getByUsername(String username) {
        LOGGER.debug("Searching account by username: [{}]", username);
        Session session = getSession();

        String hql = "from Account a where lower(a.username) = :username";
        List<Account> result = session.createQuery(hql)
                .setParameter("username", username.toLowerCase())
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Account with username [{}] cannot be found", username);
            return null;
        }
        LOGGER.debug("Account with username [{}] found, account id: [{}]", username, result.get(0).getId());
        return result.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Account getByEmail(String email) {
        LOGGER.debug("Searching account by email: [{}]", email);
        Session session = getSession();

        String hql = "from Account a where lower(a.email) = :email";
        List<Account> result = session.createQuery(hql)
                .setParameter("email", email.toLowerCase())
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Account with email [{}] cannot be found", email);
            return null;
        }
        LOGGER.debug("Account with email [{}] found, account id: [{}]", email, result.get(0).getId());
        return result.get(0);
    }

    @SuppressWarnings("unchecked")
    public Account getByCharacterName(String characterName) {
        LOGGER.debug("Searching account by character name: [{}]", characterName);
        Session session = getSession();

        String hql = "from CCharacter c where lower(c.name) = :name";
        List<CCharacter> result = session.createQuery(hql)
                .setParameter("name", characterName.toLowerCase())
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Account with character [{}] cannot be found", characterName);
            return null;
        }

        CCharacter character = result.get(0);
        Account account = getById(character.getAccountId());

        LOGGER.debug("Account with character [{}] found, account id: [{}]", characterName, account.getId());
        return account;
    }

    @SuppressWarnings("unchecked")
    public List<CCharacter> getCharactersByAccountId(int id) {
        LOGGER.debug("Searching characters by account id: [{}]", id);
        Session session = getSession();

        String hql = "from CCharacter c where c.accountId = :id";
        List<CCharacter> result = session.createQuery(hql)
                .setParameter("id", id)
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Character for account id [{}] cannot be found", id);
            return null;
        }

        LOGGER.debug("Found [{}] Characters for account id: [{}]", result.size(), id);
        return result;
    }
}
