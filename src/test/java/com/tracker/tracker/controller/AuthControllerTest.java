package com.tracker.tracker.controller;

import com.tracker.tracker.entity.User;
import com.tracker.tracker.security.SecurityConfig;
import com.tracker.tracker.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;


    @Test
    public void should_return401_when_invalidCredentialsArePassed() throws Exception {

        String malformedRequest = "{ \"login\": \"test\", \"password\": \"blabla!\" }";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedRequest))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Incorrect credentials!"));
    }

    @Test
    public void should_return200_when_credentialsAreCorrect() throws Exception {

        String validRequest = "{ \"login\": \"test-login\", \"password\": \"test-password\" }";
        User mockUser = User.builder()
                .id(1)
                .login("test-login")
                .password("test-password")
                .build();

        Mockito.when(userService.findUserByLogin(mockUser.getLogin())).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.type").value("Bearer"));
    }

    @Test
    public void should_return401_whenGivenUserDoesNotExist() throws Exception {

        String request = "{ \"login\": \"test-login\", \"password\": \"test-password\" }";

        Mockito.when(userService.findUserByLogin("test-login")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isUnauthorized());

    }

}
