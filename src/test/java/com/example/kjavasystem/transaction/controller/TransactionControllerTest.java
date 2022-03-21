package com.example.kjavasystem.transaction.controller;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.transaction.request.TransactionReceiveRequest;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.transaction.service.TransactionService;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TransactionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private EmployeeRepository employeeRepository;

    final static String MOCK_USER = "test";
    final static String MOCK_PASSWORD = "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6";
    final static String MOCK_JWT = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0IiwiaWF0IjoxNjQ3ODc3MzAwfQ.34-Tg4Ba5VJzvf6th_DEszLWsb8RhyrsRF167fS-jsmFCqivc25FVEo_RsOcngKKsmDampznDcAo543A5EGQPw";

    @BeforeEach
    void setUp() {
        Employee employee = new Employee();
        employee.setUserName(MOCK_USER);
        employee.setPassword(MOCK_PASSWORD);
        when(employeeRepository.findByUsername(anyString())).thenReturn(Optional.of(employee));

        testRestTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Authorization", "Bearer " + MOCK_JWT);
                    return execution.execute(request, body);
                }));
    }

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

    @Test
    @DisplayName("test case post updateTransaction success.")
    void test_updateTransaction_success() {
        TransactionReceiveRequest transactionReceiveRequest = new TransactionReceiveRequest();
        transactionReceiveRequest.setTransactionId(1);
        transactionReceiveRequest.setReceiveBy(1);
        transactionReceiveRequest.setReceiveBranchId(1);
        transactionReceiveRequest.setMoneyBoxId("1234");
        transactionReceiveRequest.setTotalMoney(100.00);
        doNothing().when(transactionService).updateTransaction(any(TransactionReceiveRequest.class));

        Response actual = testRestTemplate.postForObject("/transaction/update", transactionReceiveRequest, Response.class);

        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
        assertEquals("", actual.getData());
    }

}