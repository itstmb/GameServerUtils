package com.gameserver.utils.account.dao;

import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.CCharacter;

import java.util.List;

public interface AccountDao {

    Integer save(Account account) throws RuntimeException;

    void deleteById(int id) throws RuntimeException;

    Account getById(int id);

    Account getByUsername(String username);

    Account getByEmail(String email);

    Account getByCharacterName(String characterName);

    List<CCharacter> getCharactersByAccountId(int id);
}
