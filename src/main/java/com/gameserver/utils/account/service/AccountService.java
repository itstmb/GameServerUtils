package com.gameserver.utils.account.service;

import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordAndKeyRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithKeyRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPICRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPasswordRequest;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.Map;

public interface AccountService {

    AccountResponse register(AccountRegisterWithKeyRequest accountRegisterWithKeyRequest) throws Exception;

    AccountResponse register(AccountRegisterWithDiscordRequest accountRegisterWithDiscordRequest) throws Exception;

    AccountResponse register(AccountRegisterWithDiscordAndKeyRequest accountRegisterWithDiscordAndKeyRequest) throws Exception;

    @SuppressWarnings("unused")
    AccountResponse resetPassword(ResetPasswordRequest resetPasswordRequest);

    ResponseEntity<Map<String, Object>> resetPIC(ResetPICRequest resetPICRequest) throws ParseException;
}
