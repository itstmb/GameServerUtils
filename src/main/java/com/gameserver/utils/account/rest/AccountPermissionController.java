package com.gameserver.utils.account.rest;

import com.gameserver.utils.account.service.AccountPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/account/permission")
public class AccountPermissionController {

    private final AccountPermissionService accountPermissionService;

    @Autowired
    public AccountPermissionController(AccountPermissionService accountPermissionService) {
        this.accountPermissionService = accountPermissionService;
    }

    @RequestMapping(value = "/discord", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    Map<String, Object> getDiscordVerifiedAccounts() {
        return accountPermissionService.getDiscordVerifiedAccounts();
    }
}
