package com.gameserver.utils.account.rest;

import com.gameserver.utils.account.rest.requests.discord.AccountDiscordConnectionRequest;
import com.gameserver.utils.account.rest.requests.discord.KeyDiscordConnectionRequest;
import com.gameserver.utils.account.rest.responses.AccountResponse;
import com.gameserver.utils.account.service.AccountDiscordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Map;

import static com.gameserver.utils.account.rest.requests.common.validation.CommonCharacterChecks.CHARACTER_NAME_MAX_LENGTH;
import static com.gameserver.utils.account.rest.requests.common.validation.CommonCharacterChecks.CHARACTER_NAME_MIN_LENGTH;

@RestController
@RequestMapping(value = "/api/account/discord")
public class AccountDiscordController {
    private final AccountDiscordService accountDiscordService;

    @Autowired
    public AccountDiscordController(AccountDiscordService accountDiscordService) {
        this.accountDiscordService = accountDiscordService;
    }

    @RequestMapping(value = "/connect/username", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountResponse connectDiscord(@Valid @RequestBody AccountDiscordConnectionRequest accountDiscordConnectionRequest) {
        return accountDiscordService.connectDiscord(accountDiscordConnectionRequest);
    }

    @RequestMapping(value = "/connect/key", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountResponse connectDiscord(@Valid @RequestBody KeyDiscordConnectionRequest keyDiscordConnectionRequest) {
        return accountDiscordService.connectDiscord(keyDiscordConnectionRequest);
    }

    @RequestMapping(value = "/{discordId}/username", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    ResponseEntity<Map<String, Object>> getUsername(@PathVariable @NotBlank @Size(min = 17, max = 18) String discordId) {
        return accountDiscordService.getUsername(discordId);
    }

    @RequestMapping(value = "/{discordId}/characters", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountResponse getCharacters(@PathVariable("discordId") @NotBlank @Size(min = 17, max = 18) String discordId) {
        return accountDiscordService.getCharacters(discordId);
    }

    @RequestMapping(value = "/character/{characterName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody
    AccountResponse getDiscord(@PathVariable("characterName") @NotBlank @Size(min = CHARACTER_NAME_MIN_LENGTH, max = CHARACTER_NAME_MAX_LENGTH) String characterName) {
        return accountDiscordService.getDiscord(characterName);
    }
}
