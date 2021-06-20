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

import static com.gameserver.utils.utils.TestConstants.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = GameServerUtilsApplication.class)
public class resetPasswordTest {

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
        Mockito.when(accountDiscordDaoMock.getByAccountId(CREATED_ACCOUNT_ID)).thenReturn(EXISTING_ACCOUNT_DISCORD);
        Mockito.when(accountDiscordDaoMock.getByDiscordId(TAKEN_DISCORD_ID)).thenReturn(EXISTING_ACCOUNT_DISCORD);
        Mockito.when(accountDiscordDaoMock.getByDiscordId(TAKEN_DISCORD_ID2)).thenReturn(EXISTING_ACCOUNT_DISCORD2);
    }

    @Test
    public void resetPasswordWithValidParameters() throws Exception {
        String requestJson = generateResetPasswordRequest(TAKEN_USERNAME, TAKEN_DISCORD_ID, VALID_PASSWORD);

        performResetPassword(requestJson, STATUS_OK);
    }

    @Test
    public void resetPasswordWithNonexistentUsername() throws Exception {
        String requestJson = generateResetPasswordRequest(VALID_USERNAME, TAKEN_DISCORD_ID, VALID_PASSWORD);

        performResetPassword(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void resetPasswordWithNonexistentDiscord() throws Exception {
        String requestJson = generateResetPasswordRequest(TAKEN_USERNAME, VALID_DISCORD_ID, VALID_PASSWORD);

        performResetPassword(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void resetPasswordWithInvalidPassword() throws Exception {
        String requestJson = generateResetPasswordRequest(TAKEN_USERNAME, TAKEN_DISCORD_ID, SHORT_PASSWORD);

        performResetPassword(requestJson, STATUS_FORBIDDEN);
    }

    @Test
    public void resetPasswordWithUnmatchingUsernameAndDiscord() throws Exception {
        String requestJson = generateResetPasswordRequest(TAKEN_USERNAME, TAKEN_DISCORD_ID2, VALID_PASSWORD);

        performResetPassword(requestJson, STATUS_FORBIDDEN);
    }

    private void performResetPassword(String requestJson, ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(post("/api/account/reset/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(expectedResult);
    }

    private String generateResetPasswordRequest(String username, String discord, String newPassword) {

        return "{\n" +
                "    \"username\": \"" + username + "\",\n" +
                "    \"discord\": \"" + discord + "\",\n" +
                "    \"newPassword\": \"" + newPassword + "\"\n" +
                "}";
    }
}
