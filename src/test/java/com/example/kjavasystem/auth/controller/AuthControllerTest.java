package com.example.kjavasystem.auth.controller;

import com.example.kjavasystem.auth.service.JwtUserDetailsService;
import com.example.kjavasystem.config.JwtTokenUtil;
import com.example.kjavasystem.management.exception.CannotAccessDataException;
import com.example.kjavasystem.management.reqeust.UpdateBankMoneyRequest;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.JwtRequest;
import com.example.kjavasystem.utils.model.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    @DisplayName("test case authenticate success.")
    void test_authenticate_success() {
        User user = new User("test", "1234", new ArrayList<>());
        JwtRequest jwtRequest = new JwtRequest("test", "1234");
        when(jwtUserDetailsService.loadUserByUsername(anyString())).thenReturn(user);

        Response actual = testRestTemplate.postForObject("/authenticate", jwtRequest, Response.class);

        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
    }
}