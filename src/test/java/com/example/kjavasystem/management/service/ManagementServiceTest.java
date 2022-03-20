package com.example.kjavasystem.management.service;

import com.example.kjavasystem.management.exception.CannotAccessDataException;
import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.transaction.entity.BankMoney;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.UpdateTransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.BankMoneyRepository;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.service.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ManagementServiceTest {
    @Mock
    BankMoneyRepository bankMoneyRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("test case getTotalMoneyFromBranch success")
    void test_getTotalMoneyFromBranch_success() {
        ManagementService managementService = new ManagementService();
        managementService.setEmployeeRepository(employeeRepository);
        managementService.setBankMoneyRepository(bankMoneyRepository);

        Employee employee = new Employee();
        employee.setRoleId(3);

        BankMoney bankMoney = new BankMoney();
        bankMoney.setBranchId(1);
        bankMoney.setTotalMoney(100.00);

        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(bankMoneyRepository.findByBranchId(anyInt())).thenReturn(Optional.of(bankMoney));

        TotalMoneyResponse actual = managementService.getTotalMoneyFromBranch(1, 1);

        assertEquals(100.00, actual.getTotalMoney());
        assertEquals(1, actual.getBranchId());
    }

    @Test
    @DisplayName("test case getTotalMoneyFromBranch cannot access branch")
    void test_getTotalMoneyFromBranch_cannot_access_branch() {
        ManagementService managementService = new ManagementService();
        managementService.setEmployeeRepository(employeeRepository);
        managementService.setBankMoneyRepository(bankMoneyRepository);

        Employee employee = new Employee();
        employee.setRoleId(3);

        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(bankMoneyRepository.findByBranchId(anyInt())).thenReturn(Optional.empty());

        CannotAccessDataException exception = assertThrows(CannotAccessDataException.class, () -> managementService.getTotalMoneyFromBranch(1, 1));

        String expectedMessage = "Cannot access data of this branch.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("test case getTotalMoneyFromBranch cannot access data")
    void test_getTotalMoneyFromBranch_cannot_access_data() {
        ManagementService managementService = new ManagementService();
        managementService.setEmployeeRepository(employeeRepository);

        Employee employee = new Employee();
        employee.setRoleId(3);

        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        CannotAccessDataException exception = assertThrows(CannotAccessDataException.class, () -> managementService.getTotalMoneyFromBranch(1, 1));

        String expectedMessage = "Your role cannot access data.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}