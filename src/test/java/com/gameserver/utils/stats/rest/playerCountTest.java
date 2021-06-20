package com.gameserver.utils.stats.rest;

import com.gameserver.utils.GameServerUtilsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration(classes = GameServerUtilsApplication.class)
class playerCountTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RestTemplate restTemplateMock;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPlayerCount() throws Exception {
        LinkedHashMap<String, Integer> playerCountServerRunningResponse = new LinkedHashMap<>();
        playerCountServerRunningResponse.put("characters", 5);
        playerCountServerRunningResponse.put("accounts", 3);
        Mockito.when(restTemplateMock.getForObject(Mockito.anyString(), Mockito.any())).thenReturn(playerCountServerRunningResponse);
        performPlayerCount(status().isOk());
    }

    @Test
    public void getPlayerCountGameServerOffline() throws Exception {
        Mockito.when(restTemplateMock.getForObject(Mockito.anyString(), Mockito.any())).thenThrow(RestClientException.class);
        performPlayerCount(status().isOk());
    }

    private void performPlayerCount(ResultMatcher expectedResult) throws Exception {
        mockMvc.perform(get("/api/stats/playerCount"))
                .andExpect(expectedResult);
    }
}