package com.gameserver.utils.account.rest;

import com.gameserver.utils.GameServerUtilsApplication;
import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.AccountDiscordDao;
import com.gameserver.utils.account.dao.KeyDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static com.gameserver.utils.utils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = GameServerUtilsApplication.class)
public class connectDiscordTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDao accountDaoMock;

    @MockBean
    private KeyDao keyDaoMock;

    @MockBean
    private AccountDiscordDao accountDiscordDaoMock;

    private static final String BY_USERNAME = "username";
    private static final String BY_KEY = "key";

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupMockListeners();
    }

    private void setupMockListeners() {
        Mockito.when(accountDaoMock.getByUsername(TAKEN_USERNAME)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDaoMock.getById(EXISTING_ACCOUNT_ID)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDiscordDaoMock.getByAccountId(0)).thenReturn(null);
        Mockito.when(accountDiscordDaoMock.getByDiscordId(TAKEN_DISCORD_ID)).thenReturn(EXISTING_ACCOUNT_DISCORD);
        Mockito.when(keyDaoMock.getBySerialNumber(TAKEN_SERIAL_KEY)).thenReturn(TAKEN_KEY);
    }

    @Test
    public void connectDiscordByUsernameValid() throws Exception {
        String requestJson = generateConnectDiscordByUsernameRequest(TAKEN_USERNAME, VALID_DISCORD_ID);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_OK);
    }

    @Test
    public void connectDiscordByUsernameWithEmptyParameters() throws Exception {
        String requestJson = generateConnectDiscordByUsernameRequest(EMPTY_STRING, VALID_DISCORD_ID);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);

        requestJson = generateConnectDiscordByUsernameRequest(TAKEN_USERNAME, EMPTY_STRING);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordWithDiscordOnly() throws Exception {
        String requestJson = generateConnectDiscordRequestWithDiscordOnly(VALID_DISCORD_ID);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordByUsernameWithInvalidUsernames() throws Exception {
        String requestJson;

        for (String invalidUsername : INVALID_USERNAMES) {
            requestJson = generateConnectDiscordByUsernameRequest(invalidUsername, VALID_DISCORD_ID);
            performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void connectDiscordByUsernameWithNonexistentUsername() throws Exception {
        String requestJson = generateConnectDiscordByUsernameRequest(VALID_USERNAME, VALID_DISCORD_ID);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordByUsernameWithoutDiscord() throws Exception {
        String requestJson = generateConnectDiscordByUsernameRequestWithoutDiscordId(TAKEN_USERNAME);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordWithInvalidDiscordIds() throws Exception {
        String requestJson;

        for (String invalidDiscordId : INVALID_DISCORD_IDS) {
            requestJson = generateConnectDiscordByUsernameRequest(TAKEN_USERNAME, invalidDiscordId);
            performConnectDiscord(BY_USERNAME, requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void connectDiscordByUsernameWithTakenDiscord() throws Exception {
        String requestJson = generateConnectDiscordByUsernameRequest(TAKEN_USERNAME, TAKEN_DISCORD_ID);
        performConnectDiscord(BY_USERNAME, requestJson, STATUS_CONFLICT);
    }

    @Test
    public void connectDiscordByKeyValid() throws Exception {
        String requestJson = generateConnectDiscordByKeyRequest(TAKEN_SERIAL_KEY, VALID_DISCORD_ID);
        performConnectDiscord(BY_KEY, requestJson, STATUS_OK);
    }

    @Test
    public void connectDiscordByKeyWithEmptyParameters() throws Exception {
        String requestJson = generateConnectDiscordByKeyRequest(EMPTY_STRING, VALID_DISCORD_ID);
        performConnectDiscord(BY_KEY, requestJson, STATUS_FORBIDDEN);

        requestJson = generateConnectDiscordByKeyRequest(TAKEN_SERIAL_KEY, EMPTY_STRING);
        performConnectDiscord(BY_KEY, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordByKeyWithInvalidKeyFormats() throws Exception {
        String requestJson;

        for (String invalidSerialKey : INVALID_SERIAL_KEY_FORMATS) {
            requestJson = generateConnectDiscordByKeyRequest(invalidSerialKey, VALID_DISCORD_ID);
            performConnectDiscord(BY_KEY, requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void connectDiscordByKeyWithNonexistentKey() throws Exception {
        String requestJson = generateConnectDiscordByKeyRequest(VALID_SERIAL_KEY, VALID_DISCORD_ID);
        performConnectDiscord(BY_KEY, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordByKeyWithoutDiscord() throws Exception {
        String requestJson = generateConnectDiscordByKeyRequestWithoutDiscordId(TAKEN_SERIAL_KEY);
        performConnectDiscord(BY_KEY, requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void connectDiscordByKeyWithTakenDiscord() throws Exception {
        String requestJson = generateConnectDiscordByKeyRequest(TAKEN_SERIAL_KEY, TAKEN_DISCORD_ID);
        performConnectDiscord(BY_KEY, requestJson, STATUS_CONFLICT);
    }

    private void performConnectDiscord(String requestType, String requestJson, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(post("/api/account/discord/connect/" + requestType)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(expectedResult);
    }

    private String generateConnectDiscordByUsernameRequest(String username, String discordId) {
        return "{\n" +
                "    \"username\": \"" + username + "\",\n" +
                "    \"discordId\": \"" + discordId + "\"\n" +
                "}";
    }

    private String generateConnectDiscordRequestWithDiscordOnly(String discordId) {
        return "{\n" +
                "    \"discordId\": \"" + discordId + "\"\n" +
                "}";
    }

    private String generateConnectDiscordByUsernameRequestWithoutDiscordId(String username) {
        return "{\n" +
                "    \"username\": \"" + username + "\"\n" +
                "}";
    }

    private String generateConnectDiscordByKeyRequestWithoutDiscordId(String key) {
        return "{\n" +
                "    \"key\": \"" + key + "\"\n" +
                "}";
    }

    private String generateConnectDiscordByKeyRequest(String key, String discordId) {
        return "{\n" +
                "    \"key\": \"" + key + "\",\n" +
                "    \"discordId\": \"" + discordId + "\"\n" +
                "}";
    }
}
