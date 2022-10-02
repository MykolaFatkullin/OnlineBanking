package com.example.onlinebanking.controller;

import static com.example.onlinebanking.data.AuthenticationDataReceiver.getJwtResponse;
import static com.example.onlinebanking.data.AuthenticationDataReceiver.getLoginRequest;
import static com.example.onlinebanking.data.AuthenticationDataReceiver.getRegistrationDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.onlinebanking.AbstractIntegrationTest;
import com.example.onlinebanking.model.dto.response.JwtResponse;
import com.example.onlinebanking.service.security.AuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @Test
    @Sql(value = {"classpath:db/init/clearAll.sql"})
    @SneakyThrows
    void registerUser() {
        var requestBody = getRegistrationDto();

        mockMvc.perform(
                        post("/auth/registration")
                                .content(objectMapper.writeValueAsString(requestBody))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    @Sql(value = {"classpath:db/init/clearAll.sql", "classpath:db/init/initUserForLogin.sql"})
    @SneakyThrows
    void login() {
        var requestBody = getLoginRequest();
        var expected = getJwtResponse();

        when(authenticationService.loginUser(requestBody)).thenReturn(expected);

        var mvcResult = mockMvc.perform(
                        post("/auth/login")
                                .content(objectMapper.writeValueAsString(requestBody))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        final var body = mvcResult.getResponse().getContentAsString();

        final var actual = objectMapper.readValue(body, JwtResponse.class);

        assertNotNull(actual);
        assertEquals(expected, actual);
    }
}
