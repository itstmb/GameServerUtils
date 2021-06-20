package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.AccountDiscord;
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
public class AccountDiscordDaoImpl implements AccountDiscordDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountDiscordDaoImpl.class); // TODO - @Slf4j annotation instead

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public AccountDiscordDaoImpl() {
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public void save(AccountDiscord accountDiscord) throws RuntimeException {
        LOGGER.debug("Saving account [{}] <-> discord [{}] relation", accountDiscord.getAccountId(), accountDiscord.getDiscordId());
        Session session = getSession();
        try {
            int accountId = (int) session.save(accountDiscord);
            LOGGER.info("AccountDiscord [{}]<->[{}] saved successfully",
                    accountDiscord.getAccountId(), accountDiscord.getDiscordId());
        } catch (Exception e) {
            LOGGER.error("Could not save AccountDiscord [{}]<->[{}] due to exception: {}",
                    accountDiscord.getAccountId(), accountDiscord.getDiscordId(), e);
            throw new RuntimeException("AccountDiscord save failed");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountDiscord getByAccountId(int accountId) {
        LOGGER.debug("Searching AccountDiscord by account id: [{}]", accountId);
        Session session = getSession();

        String hql = "from AccountDiscord a where a.accountId = :accountId";
        List<AccountDiscord> result = session.createQuery(hql)
                .setParameter("accountId", accountId)
                .list();

        if (result.size() == 0) {
            LOGGER.debug("AccountDiscord with account id [{}] cannot be found", accountId);
            return null;
        }
        LOGGER.debug("AccountDiscord with account id [{}] found, discord id: [{}]", accountId, result.get(0).getDiscordId());
        return result.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public AccountDiscord getByDiscordId(String discordId) {
        LOGGER.debug("Searching AccountDiscord by discord id: [{}]", discordId);
        Session session = getSession();

        String hql = "from AccountDiscord a where a.discordId = :discordId";
        List<AccountDiscord> result = session.createQuery(hql)
                .setParameter("discordId", discordId)
                .list();

        if (result.size() == 0) {
            LOGGER.debug("AccountDiscord with discord id [{}] cannot be found", discordId);
            return null;
        }
        LOGGER.debug("AccountDiscord with discord id [{}] found, account id: [{}]", discordId, result.get(0).getAccountId());
        return result.get(0);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<AccountDiscord> getAll() {
        LOGGER.trace("Querying the entire AccountDiscord table for entries");
        try {
        Session session = getSession();

        String hql = "from AccountDiscord a";
            return session.createQuery(hql).list();
        } catch (Exception e) {
            LOGGER.error("Failed to query the database for AccountDiscords. exception was: {0}", e);
            return null;
        }
    }
}
