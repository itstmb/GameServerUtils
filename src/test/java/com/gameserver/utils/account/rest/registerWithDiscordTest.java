package com.gameserver.utils.account.rest;

import com.gameserver.utils.GameServerUtilsApplication;
import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.AccountDiscordDao;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.gameserver.utils.utils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = GameServerUtilsApplication.class)
public class registerWithDiscordTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDao accountDaoMock;

    @MockBean
    private AccountDiscordDao accountDiscordDaoMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupMockListeners();
    }

    private void setupMockListeners() {
        Mockito.when(accountDaoMock.getByUsername(TAKEN_USERNAME)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDaoMock.getByEmail(TAKEN_EMAIL)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDaoMock.getById(CREATED_ACCOUNT_ID)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDiscordDaoMock.getByAccountId(0)).thenReturn(null);
        Mockito.when(accountDiscordDaoMock.getByDiscordId(TAKEN_DISCORD_ID)).thenReturn(EXISTING_ACCOUNT_DISCORD);
    }

    @Test
    public void registerWithDiscordWithValidUser() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_CREATED);
    }

    @Test
    public void registerWithDiscordWithEmptyParameters() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(EMPTY_STRING, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, EMPTY_STRING,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                EMPTY_STRING, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, EMPTY_STRING, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithoutUsername() throws Exception {
        String requestJson = generateRegisterWithDiscordRequestWithoutUsername(VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithShortUsername() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(SHORT_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithLongUsername() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(LONG_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithInvalidUsernames() throws Exception {
        String requestJson;

        for (String invalidUsername : INVALID_USERNAMES) {
            requestJson = generateRegisterWithDiscordRequest(invalidUsername, VALID_EMAIL,
                    VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

            performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithDiscordWithTakenUsername() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(TAKEN_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_CONFLICT);
    }

    @Test
    public void registerWithDiscordWithInvalidEmails() throws Exception {
        String requestJson;

        for (String invalidEmail : INVALID_EMAILS) {
            requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, invalidEmail,
                    VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

            performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithDiscordWithLongEmail() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, LONG_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithTakenEmail() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, TAKEN_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_CONFLICT);
    }

    @Test
    public void registerWithDiscordWithShortPassword() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                SHORT_PASSWORD, VALID_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWithInvalidPasswords() throws Exception {
        String requestJson;

        for (String invalidPassword : INVALID_PASSWORDS) {
            requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                    invalidPassword, VALID_BIRTHDAY, VALID_DISCORD_ID);

            performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithDiscordWithInvalidBirthdays() throws Exception {
        String requestJson;

        for (String invalidBirthday : INVALID_BIRTHDAYS) {
            requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                    VALID_PASSWORD, invalidBirthday, VALID_DISCORD_ID);

            performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithDiscordWithYoungBirthday() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, YOUNG_BIRTHDAY, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithDiscordWith1DayOlderThanRequiredBirthday() throws Exception {
        LocalDate birthday = LocalDate.now().minusYears(13).minusDays(1);
        String birthdayStr = (birthday).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, birthdayStr, VALID_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_CREATED);
    }

    @Test
    public void connectDiscordWithInvalidDiscordIds() throws Exception {
        String requestJson;

        for (String invalidDiscordId : INVALID_DISCORD_IDS) {
            requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                    VALID_PASSWORD, VALID_BIRTHDAY, invalidDiscordId);

            performRegisterWithDiscord(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithKeyWithTakenDiscord() throws Exception {
        String requestJson = generateRegisterWithDiscordRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, TAKEN_DISCORD_ID);

        performRegisterWithDiscord(requestJson, STATUS_CONFLICT);
    }

    private void performRegisterWithDiscord(String requestJson, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(post("/api/account/register/discord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(expectedResult);
    }

    private String generateRegisterWithDiscordRequest(String username, String email, String password,
                                                      String birthday, String discord) {

        return "{\n" +
                "    \"account\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"birthday\": \"" + birthday + "\"\n" +
                "    },\n" +
                "    \"discord\": \"" + discord + "\"\n" +
                "}";
    }

    private String generateRegisterWithDiscordRequestWithoutUsername(String email, String password,
                                                                     String birthday, String discord) {

        return "{\n" +
                "    \"account\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"birthday\": \"" + birthday + "\"\n" +
                "    },\n" +
                "    \"discord\": \"" + discord + "\"\n" +
                "}";
    }

    private String generateRegisterWithDiscordRequestWithoutKey(String username, String email, String password,
                                                                String birthday) {

        return "{\n" +
                "    \"account\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"birthday\": \"" + birthday + "\"\n" +
                "    }\n" +
                "}";
    }
}