package com.gameserver.utils.account.rest;

import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordAndKeyRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithDiscordRequest;
import com.gameserver.utils.account.rest.requests.register.AccountRegisterWithKeyRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPICRequest;
import com.gameserver.utils.account.rest.requests.reset.ResetPasswordRequest;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import com.gameserver.utils.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Deprecated
    @RequestMapping(value = "/registerWithKey", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    AccountResponse v1RegisterWithKey(@Valid @RequestBody AccountRegisterWithKeyRequest accountRegisterWithKeyRequest) throws Exception {
        return accountService.register(accountRegisterWithKeyRequest);
    }

    @RequestMapping(value = "/register/key", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    AccountResponse registerWithKey(@Valid @RequestBody AccountRegisterWithKeyRequest accountRegisterWithKeyRequest) throws Exception {
        return accountService.register(accountRegisterWithKeyRequest);
    }

    @RequestMapping(value = "/register/discord", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    AccountResponse registerWithDiscord(@Valid @RequestBody AccountRegisterWithDiscordRequest accountRegisterWithDiscordRequest) throws Exception {
        return accountService.register(accountRegisterWithDiscordRequest);
    }

    @RequestMapping(value = "/register/discordAndKey", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody
    AccountResponse registerWithDiscordAndKey(@Valid @RequestBody AccountRegisterWithDiscordAndKeyRequest accountRegisterWithDiscordAndKeyRequest) throws Exception {
        return accountService.register(accountRegisterWithDiscordAndKeyRequest);
    }

    @RequestMapping(value = "/reset/password", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountResponse resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        return accountService.resetPassword(resetPasswordRequest);
    }

    @RequestMapping(value = "/reset/pic", method = RequestMethod.PATCH)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> resetPIC(@Valid @RequestBody ResetPICRequest resetPICRequest) throws ParseException {
        return accountService.resetPIC(resetPICRequest);
    }
}
