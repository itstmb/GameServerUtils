package com.gameserver.utils.account.service;

import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.AccountDiscordDao;
import com.gameserver.utils.account.dao.KeyDao;
import com.gameserver.utils.account.entity.Account;
import com.gameserver.utils.account.entity.AccountDiscord;
import com.gameserver.utils.account.entity.Key;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordAndKeyRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithKeyRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPICRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPasswordRequest;
import com.gameserver.utils.account.rest.responses.AccountErrorException;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import com.gameserver.utils.account.rest.responses.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

@Service
@EnableTransactionManagement
public class AccountServiceImpl implements AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountServiceImpl.class); // TODO - @Slf4j annotation instead

    private final AccountDiscordService accountDiscordService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final AccountDao accountDao;
    private final KeyDao keyDao;
    private final PasswordEncoder passwordEncoder;
    private final AccountDiscordDao accountDiscordDao;

    @Autowired
    public AccountServiceImpl(AccountDiscordService accountDiscordService, AccountDao accountDao, KeyDao keyDao, AccountDiscordDao accountDiscordDao, PasswordEncoder passwordEncoder) {
        this.accountDiscordService = accountDiscordService;
        this.accountDao = accountDao;
        this.keyDao = keyDao;
        this.accountDiscordDao = accountDiscordDao;
        this.passwordEncoder = passwordEncoder;
    }

    private Integer registerAccount(AccountRegisterRequest accountRegisterRequest) throws ParseException {
        Date birthday = parseDate(accountRegisterRequest.getBirthday());

        Account account = new Account(accountRegisterRequest.getUsername(),
                accountRegisterRequest.getEmail(),
                passwordEncoder.encode(accountRegisterRequest.getPassword()),
                birthday);

        return accountDao.save(account);
    }

    // TODO: add ModelMapper Support
    @Override
    @Transactional
    public AccountResponse register(AccountRegisterWithKeyRequest accountRegisterWithKeyRequest) throws Exception {
        LOGGER.debug("Registering account [{}] with key [{}]", accountRegisterWithKeyRequest.getUsername(),
                accountRegisterWithKeyRequest.getKey());

        Integer accountId = registerAccount(accountRegisterWithKeyRequest);
        useKey(accountRegisterWithKeyRequest.getKey(), accountId);

        AccountResponse accountResponse = new AccountResponse(HttpStatus.CREATED.value(), "Account created successfully");
        LOGGER.info("Account [{}] registered successfully with key [{}], response was: {}",
                accountId, accountRegisterWithKeyRequest.getKey(), accountResponse.toString());
        return accountResponse;
    }

    @Override
    @Transactional
    public AccountResponse register(AccountRegisterWithDiscordRequest accountRegisterWithDiscordRequest) throws Exception {
        LOGGER.debug("Registering account [{}] with discord [{}]", accountRegisterWithDiscordRequest.getUsername(),
                accountRegisterWithDiscordRequest.getDiscord());

        Integer accountId = registerAccount(accountRegisterWithDiscordRequest);
        accountDiscordService.connectDiscord(accountDao.getById(accountId), accountRegisterWithDiscordRequest.getDiscord());

        AccountResponse accountResponse = new AccountResponse(HttpStatus.CREATED.value(), "Account created successfully");
        LOGGER.info("Account [{}] registered successfully with discord [{}], response was: {}",
                accountId, accountRegisterWithDiscordRequest.getDiscord(), accountResponse.toString());
        return accountResponse;
    }

    @Override
    @Transactional
    public AccountResponse register(AccountRegisterWithDiscordAndKeyRequest accountRegisterWithDiscordAndKeyRequest) throws Exception {
        LOGGER.debug("Registering account [{}] with discord [{}] and key [{}]", accountRegisterWithDiscordAndKeyRequest.getUsername(),
                accountRegisterWithDiscordAndKeyRequest.getDiscord(),
                accountRegisterWithDiscordAndKeyRequest.getKey());

        Integer accountId = registerAccount(accountRegisterWithDiscordAndKeyRequest);
        accountDiscordService.connectDiscord(accountDao.getById(accountId), accountRegisterWithDiscordAndKeyRequest.getDiscord());
        useKey(accountRegisterWithDiscordAndKeyRequest.getKey(), accountId);

        AccountResponse accountResponse = new AccountResponse(HttpStatus.CREATED.value(), "Account created successfully");
        LOGGER.info("Account [{}] registered successfully with discord [{}] and key [{}], response was: {}",
                accountId, accountRegisterWithDiscordAndKeyRequest.getDiscord(),
                accountRegisterWithDiscordAndKeyRequest.getKey(), accountResponse.toString());
        return accountResponse;
    }

    @Override
    public AccountResponse resetPassword(ResetPasswordRequest resetPasswordRequest) {
        LOGGER.debug("Resetting password for account [{}] with discord [{}]", resetPasswordRequest.getUsername(),
                resetPasswordRequest.getDiscordId());

        Account account = accountDao.getByUsername(resetPasswordRequest.getUsername());

        AccountDiscord accountDiscord = accountDiscordService.getByAccount(account.getId());
        if (accountDiscord == null || !accountDiscord.getDiscordId().equals(resetPasswordRequest.getDiscordId())) {
            throw new AccountErrorException("Username doesn't match discord", HttpStatus.FORBIDDEN);
        }

        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        accountDao.save(account);

        AccountResponse accountResponse = new AccountResponse(HttpStatus.OK.value(), "Password reset successfully");
        LOGGER.info("Account [{}] password has been changed successfully, response was {}",
                account.getUsername(), accountResponse.toString());
        return accountResponse;
    }

    @Override
    public ResponseEntity<Map<String, Object>> resetPIC(ResetPICRequest resetPICRequest) throws ParseException {
        LOGGER.debug("Resetting PIC for account with discord {}", resetPICRequest.getDiscordId());

        Date birthday = parseDate(resetPICRequest.getBirthday()); // At this point we are already past validation, so this can't throw an exception

        AccountDiscord accountDiscord = accountDiscordDao.getByDiscordId(resetPICRequest.getDiscordId());
        Account account = accountDao.getById(accountDiscord.getAccountId());

        if (account == null || !account.getBirthday().equals(birthday)) {
            throw new JSONException("Incorrect birthday provided", HttpStatus.UNAUTHORIZED);
        }

        if (account.getGmLevel() > 0) {
            throw new JSONException("Unauthorised operation", HttpStatus.UNAUTHORIZED);
        }

        if (account.getData() == null) {
            throw new JSONException("No PIC defined for the account", HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> data = account.getData();
        data.remove("core.pic.code");
        data.put("core.pic.code", resetPICRequest.getNewPIC());

        account.setData(data);
        accountDao.save(account);

        LOGGER.info("Account [{}] PIC has been changed successfully", account.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("message", "PIC changed successfully");

        return ResponseEntity.ok(response);
    }

    private Date parseDate(String dateString) throws ParseException {
        LOGGER.debug("Parsing date [{}]", dateString);
        java.util.Date parsedDate = dateFormat.parse(dateString);
        return new Date(parsedDate.getTime());
    }

    private void useKey(String serialNumber, int accountId) throws Exception {
        LOGGER.debug("Attempting to use key [{}] by account id [{}]", serialNumber, accountId);
        Key key = keyDao.getBySerialNumber(serialNumber);
        if (key == null) {
            LOGGER.warn("Potential race condition: Key was found initially but missing when trying to use. Deleting account {}", accountId);
            accountDao.deleteById(accountId);
            throw new AccountErrorException("Invalid key");
        } else if (key.getAccountId() != null) {
            LOGGER.warn("Potential race condition: Key was free initially but taken when trying to use. Deleting account {}", accountId);
            accountDao.deleteById(accountId);
            throw new AccountErrorException("Key has already been used");
        } else {
            saveKey(key, accountId);
        }
    }

    private void saveKey(Key key, int accountId) throws Exception {
        LOGGER.debug("Attempting to save key id [{}] with account id [{}]", key.getId(), accountId);
        key.setAccountId(accountId);
        try {
            keyDao.save(key);
        } catch (Exception e) {
            LOGGER.error("Failed to save key, deleting account {}, exception:", accountId, e);
            accountDao.deleteById(accountId);
            throw new SQLException("Failed to save key");
        }
    }
}
