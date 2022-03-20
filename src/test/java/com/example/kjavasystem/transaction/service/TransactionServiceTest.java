package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.Branch;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.exception.RoleCannotAccessException;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.BranchRepository;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.transaction.repository.TransactionRepository;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.utils.enums.RoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Mock
    BranchRepository branchRepository;

    @Mock
    EmployeeRepository employeeRepository;

    @Test
    @DisplayName("test case create transaction success")
    void test_createTransaction_success() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCratedBy(1);

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setCreatedBy(1);
        when(transactionRepository.findFirstByOrderByIdDesc()).thenReturn(transaction);

        transactionService.createTransaction(transactionRequest);

        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    @DisplayName("test case create transaction fail")
    void test_createTransaction_fail() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCratedBy(1);

        doThrow(new RuntimeException("error")).when(transactionRepository).findFirstByOrderByIdDesc();

        CreateTransactionFailException exception = assertThrows(CreateTransactionFailException.class, () -> transactionService.createTransaction(transactionRequest));

        String expectedMessage = "error";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("test case get transaction success by role cash center staff")
    void test_getTransaction_success_role_cash_center_staff() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setBranchRepository(branchRepository);
        transactionService.setEmployeeRepository(employeeRepository);

        Transaction transaction = getMockTransaction();
        Employee employee = new Employee();
        employee.setRoleId(RoleEnum.CASH_CENTER_STAFF.getRoleId());
        Branch branch1 = new Branch();
        branch1.setName("cash center");

        Branch branch2 = new Branch();
        branch2.setName("a");

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(branchRepository.findById(anyInt())).thenReturn(Optional.of(branch1)).thenReturn(Optional.of(branch2));

        TransactionDto actual = transactionService.getTransaction(1, "1234", 1);

        assertEquals(1, actual.getId());
        assertEquals("test", actual.getStatus());
        assertEquals("cash center", actual.getReceiverBranch());
        assertEquals("a", actual.getSenderBranch());
        assertEquals("1234", actual.getMoneyBoxId());
        assertEquals(100.0, actual.getTotalMoney());
    }

    @Test
    @DisplayName("test case get transaction success by role transport staff")
    void test_getTransaction_success_role_transport_staff() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setBranchRepository(branchRepository);
        transactionService.setEmployeeRepository(employeeRepository);

        Transaction transaction = getMockTransaction();
        Employee employee = new Employee();
        employee.setRoleId(RoleEnum.TRANSPORT_STAFF.getRoleId());

        Branch branch1 = new Branch();
        branch1.setName("cash center");

        Branch branch2 = new Branch();
        branch2.setName("a");

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(branchRepository.findById(anyInt())).thenReturn(Optional.of(branch1)).thenReturn(Optional.of(branch2));

        TransactionDto actual = transactionService.getTransaction(1, "1234", 1);

        assertEquals(1, actual.getId());
        assertEquals("test", actual.getStatus());
        assertEquals("cash center", actual.getReceiverBranch());
        assertEquals("a", actual.getSenderBranch());
        assertEquals("1234", actual.getMoneyBoxId());
        assertEquals(0.0, actual.getTotalMoney());
    }

    @Test
    @DisplayName("test case not found transaction")
    void test_getTransaction_not_found() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(1, "1234", 1));

        String expectedMessage = "Transaction not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("test case not found employee")
    void test_getTransaction_employee_not_found() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setEmployeeRepository(employeeRepository);

        Transaction transaction = getMockTransaction();

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.empty());

        TransactionNotFoundException exception = assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(1, "1234", 1));

        String expectedMessage = "Employee not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("test case role cannot access")
    void test_getTransaction_role_cannot_access() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setBranchRepository(branchRepository);
        transactionService.setEmployeeRepository(employeeRepository);

        Transaction transaction = getMockTransaction();
        Employee employee = new Employee();
        employee.setRoleId(3);

        Branch branch1 = new Branch();
        branch1.setName("cash center");

        Branch branch2 = new Branch();
        branch2.setName("a");

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(employeeRepository.findById(anyInt())).thenReturn(Optional.of(employee));
        when(branchRepository.findById(anyInt())).thenReturn(Optional.of(branch1)).thenReturn(Optional.of(branch2));

        RoleCannotAccessException exception = assertThrows(RoleCannotAccessException.class, () -> transactionService.getTransaction(1, "1234", 1));

        String expectedMessage = "Your role cannot access data.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    private Transaction getMockTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setReceiveBranchId(1);
        transaction.setSenderBranchId(2);
        transaction.setStatus("test");
        transaction.setMoneyBoxId("1234");
        transaction.setTotalMoney(100.00);
        return transaction;
    }
}