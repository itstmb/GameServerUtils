package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.Key;
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
public class KeyDaoImpl implements KeyDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public KeyDaoImpl() {
    }

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    @Transactional
    public void save(Key key) throws RuntimeException {
        LOGGER.debug("Saving key [{}], key serial number [{}]", key.getId(), key.getSerialNumber());
        Session session = getSession();
        try {
            int keyId = (int) session.save(key);
            LOGGER.info("Key id [{}] used by account id [{}] successfully.", keyId, key.getAccountId());
        } catch (Exception e) {
            LOGGER.error("Could not update key [{}] due to exception: {}", key.getId(), e);
            throw new RuntimeException("Key update failed");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Key getBySerialNumber(String serialNumber) {
        LOGGER.debug("Searching key by serial number: [{}]", serialNumber);
        Session session = getSession();

        String hql = "from Key k where k.serialNumber = :serialNumber";
        List<Key> result = session.createQuery(hql)
                .setParameter("serialNumber", serialNumber) // Keys are case sensitive
                .list();

        if (result.size() == 0) {
            LOGGER.debug("Key with serial number [{}] cannot be found", serialNumber);
            return null;
        }
        LOGGER.debug("Key with serial number [{}] found, key id: [{}]", serialNumber, result.get(0).getId());
        return result.get(0);
    }
}
