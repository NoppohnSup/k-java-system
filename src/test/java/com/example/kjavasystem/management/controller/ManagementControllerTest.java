package com.example.kjavasystem.management.controller;

import com.example.kjavasystem.management.reqeust.UpdateBankMoneyRequest;
import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.management.service.ManagementService;
import com.example.kjavasystem.transaction.entity.BankMoney;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ManagementControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ManagementService managementService;

    @Test
    @DisplayName("test case getTotalMoney success.")
    void test_getTotalMoney_success() {
        TotalMoneyResponse totalMoneyResponse = new TotalMoneyResponse();
        totalMoneyResponse.setTotalMoney(100.00);
        totalMoneyResponse.setBranchId(1);
        when(managementService.getTotalMoneyFromBranch(anyInt(), anyInt())).thenReturn(totalMoneyResponse);

        Response actual = testRestTemplate.getForObject("/bank_money/1?employeeId=1", Response.class);

        Map expected = objectMapper.convertValue(totalMoneyResponse, HashMap.class);
        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
        assertEquals(expected, actual.getData());
    }

    @Test
    @DisplayName("test case post updateBankMoney success.")
    void test_updateBankMoney_success() {
        UpdateBankMoneyRequest updateBankMoneyRequest = new UpdateBankMoneyRequest();
        updateBankMoneyRequest.setMoney(100);
        updateBankMoneyRequest.setBranchId(1);
        updateBankMoneyRequest.setEmployeeId(1);

        Response actual = testRestTemplate.postForObject("/bank_money/update", updateBankMoneyRequest, Response.class);

        assertEquals(MessageResponseEnum.SUCCESS.getMessage(), actual.getMessage());
        assertEquals("", actual.getData());
    }

}