package com.gameserver.utils.account.rest;

import com.gameserver.utils.GameServerUtilsApplication;
import com.gameserver.utils.account.dao.AccountDao;
import com.gameserver.utils.account.dao.KeyDao;
import com.gameserver.utils.account.entity.Key;
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
public class registerWithKeyTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountDao accountDaoMock;

    @MockBean
    private KeyDao keyDaoMock;

    private final Key VALID_KEY = new Key(1000, VALID_SERIAL_KEY, null);

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        setupMockListeners();
    }

    private void setupMockListeners() {
        Mockito.when(accountDaoMock.getByUsername(TAKEN_USERNAME)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(accountDaoMock.getByEmail(TAKEN_EMAIL)).thenReturn(EXISTING_ACCOUNT);
        Mockito.when(keyDaoMock.getBySerialNumber(VALID_SERIAL_KEY)).thenReturn(VALID_KEY);
        Mockito.when(keyDaoMock.getBySerialNumber(TAKEN_SERIAL_KEY)).thenReturn(TAKEN_KEY);
        Mockito.when(keyDaoMock.getBySerialNumber(INVALID_SERIAL_KEY)).thenReturn(null);
    }

    @Test
    public void registerWithKeyWithValidUser() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_CREATED);
    }

    @Test
    public void registerWithKeyWithEmptyParameters() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(EMPTY_STRING, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, EMPTY_STRING,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                EMPTY_STRING, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);

        requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, EMPTY_STRING, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithoutUsername() throws Exception {
        String requestJson = generateRegisterWithKeyRequestWithoutUsername(VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithShortUsername() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(SHORT_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithLongUsername() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(LONG_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithInvalidUsernames() throws Exception {
        String requestJson;

        for (String invalidUsername : INVALID_USERNAMES) {
            requestJson = generateRegisterWithKeyRequest(invalidUsername, VALID_EMAIL,
                    VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

            performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithKeyWithTakenUsername() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(TAKEN_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_CONFLICT);
    }

    @Test
    public void registerWithKeyWithInvalidEmails() throws Exception {
        String requestJson;

        for (String invalidEmail : INVALID_EMAILS) {
            requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, invalidEmail,
                    VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

            performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithKeyWithLongEmail() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, LONG_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithTakenEmail() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, TAKEN_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_CONFLICT);
    }

    @Test
    public void registerWithKeyWithShortPassword() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                SHORT_PASSWORD, VALID_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithInvalidPasswords() throws Exception {
        String requestJson;

        for (String invalidPassword : INVALID_PASSWORDS) {
            requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                    invalidPassword, VALID_BIRTHDAY, VALID_SERIAL_KEY);

            performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithKeyWithInvalidBirthdays() throws Exception {
        String requestJson;

        for (String invalidBirthday : INVALID_BIRTHDAYS) {
            requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                    VALID_PASSWORD, invalidBirthday, VALID_SERIAL_KEY);

            performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
        }
    }

    @Test
    public void registerWithKeyWithYoungBirthday() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, YOUNG_BIRTHDAY, VALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void registerWithKeyWithoutKey() throws Exception {
        String requestJson = generateRegisterWithKeyRequestWithoutKey(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY);

        performRegisterWithKey(requestJson, STATUS_UNAUTHORIZED);
    }

    @Test
    public void registerWithKeyWithInvalidKeyFormats() throws Exception {
        Mockito.when(keyDaoMock.getBySerialNumber(Mockito.anyString())).thenReturn(VALID_KEY);

        String requestJson;

        for (String invalidSerialKey : INVALID_SERIAL_KEY_FORMATS) {
            requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                    VALID_PASSWORD, VALID_BIRTHDAY, invalidSerialKey);

            performRegisterWithKey(requestJson, STATUS_UNAUTHORIZED);
        }
    }

    @Test
    public void registerWithKeyWithInvalidKey() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, INVALID_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_UNAUTHORIZED);
    }

    @Test
    public void registerWithKeyWithTakenKey() throws Exception {
        String requestJson = generateRegisterWithKeyRequest(VALID_USERNAME, VALID_EMAIL,
                VALID_PASSWORD, VALID_BIRTHDAY, TAKEN_SERIAL_KEY);

        performRegisterWithKey(requestJson, STATUS_UNAUTHORIZED);
    }

    private void performRegisterWithKey(String requestJson, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(post("/api/account/register/key")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(expectedResult);
    }

    private String generateRegisterWithKeyRequest(String username, String email, String password,
                                                  String birthday, String serialKey) {

        return "{\n" +
                "    \"account\": {\n" +
                "        \"username\": \"" + username + "\",\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"birthday\": \"" + birthday + "\"\n" +
                "    },\n" +
                "    \"key\": \"" + serialKey + "\"\n" +
                "}";
    }

    private String generateRegisterWithKeyRequestWithoutUsername(String email, String password,
                                                                 String birthday, String serialKey) {

        return "{\n" +
                "    \"account\": {\n" +
                "        \"email\": \"" + email + "\",\n" +
                "        \"password\": \"" + password + "\",\n" +
                "        \"birthday\": \"" + birthday + "\"\n" +
                "    },\n" +
                "    \"key\": \"" + serialKey + "\"\n" +
                "}";
    }

    private String generateRegisterWithKeyRequestWithoutKey(String username, String email, String password,
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