package com.example.kjavasystem.management.controller;

import com.example.kjavasystem.management.reqeust.UpdateBankMoneyRequest;
import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.management.service.ManagementService;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
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
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ManagementControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ManagementService managementService;

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