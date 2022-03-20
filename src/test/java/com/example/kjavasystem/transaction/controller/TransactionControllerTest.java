package com.example.kjavasystem.transaction.controller;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.transaction.service.TransactionService;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    @DisplayName("test case post createTransaction success.")
    void test_createTransaction_success() {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setTransactionId(1);
        transactionResponse.setMoneyBoxId("1234");
        when(transactionService.createTransaction(any(TransactionRequest.class))).thenReturn(transactionResponse);

        Response actual = testRestTemplate.postForObject("/transaction", new TransactionRequest(), Response.class);

        Map expected = objectMapper.convertValue(transactionResponse, HashMap.class);
        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
        assertEquals(expected, actual.getData());
    }

    @Test
    @DisplayName("test case getTransaction success.")
    void test_getTransaction_success() {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSenderBranch("a");
        transactionDto.setReceiverBranch("b");
        transactionDto.setId(1);
        transactionDto.setMoneyBoxId("1234");
        transactionDto.setStatus("created");

        when(transactionService.getTransaction(anyInt(), anyString(), anyInt())).thenReturn(transactionDto);
        Response actual = testRestTemplate.getForObject("/transaction/1?moneyBoxId=1&employeeId=1", Response.class);

        Map expected = objectMapper.convertValue(transactionDto, HashMap.class);
        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
        assertEquals(expected, actual.getData());
    }

}