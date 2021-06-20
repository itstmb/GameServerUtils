package com.gameserver.utils.account.rest.requests.register;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.gameserver.utils.account.rest.requests.register.validation.AccountRegisterConstraints.*;

public class AccountRegisterRequest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountRegisterRequest.class); // TODO - @Slf4j annotation instead

    public AccountRegisterRequest(Map<String, Object> account) {
        LOGGER.info("Received new request of type [{}] for account with username [{}]",
                this.getClass().getSimpleName(), account.get("username"));
        unpackNestedAccount(account);
    }

    @UsernameConstraint
    private String username;

    @EmailConstraint
    private String email;

    @PasswordConstraint
    private String password;

    @BirthdayConstraint
    private String birthday;

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthday() {
        return birthday;
    }

    public String toString() {
        return "account{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", birthday='" + birthday + '\'' +
                '}';
    }

    protected void unpackNestedAccount(Map<String, Object> account) {
        this.username = (String)account.get("username");
        this.email    = (String)account.get("email");
        this.password = (String)account.get("password");
        this.birthday = (String)account.get("birthday");
    }
}
