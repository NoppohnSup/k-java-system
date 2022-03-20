package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.*;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.exception.RoleCannotAccessException;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.exception.UpdateTransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.*;
import com.example.kjavasystem.transaction.request.TransactionReceiveRequest;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.utils.DateUtils;
import com.example.kjavasystem.utils.enums.RoleEnum;
import com.example.kjavasystem.utils.enums.TransactionStatusEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
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

    @Mock
    BankMoneyRepository bankMoneyRepository;

    @Mock
    BankMoneyHistoryRepository bankMoneyHistoryRepository;

    @Mock
    DateUtils dateUtils;

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

    @Test
    @DisplayName("test case updateTransaction success")
    void test_updateTransaction_success() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setBankMoneyRepository(bankMoneyRepository);
        transactionService.setBankMoneyHistoryRepository(bankMoneyHistoryRepository);
        transactionService.setDateUtils(dateUtils);

        TransactionReceiveRequest transactionReceiveRequest = new TransactionReceiveRequest();
        transactionReceiveRequest.setTransactionId(1);
        transactionReceiveRequest.setReceiveBy(1);
        transactionReceiveRequest.setTotalMoney(100.00);
        transactionReceiveRequest.setReceiveBranchId(1);
        transactionReceiveRequest.setMoneyBoxId("1234");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setStatus(TransactionStatusEnum.CREATED.getStatus());

        BankMoney bankMoney = new BankMoney();
        bankMoney.setId(1);
        bankMoney.setTotalMoney(100.00);
        bankMoney.setBranchId(1);
        bankMoney.setUpdatedBy(2);

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(bankMoneyRepository.findByBranchId(anyInt())).thenReturn(Optional.of(bankMoney));
        when(dateUtils.getCurrentDate()).thenReturn(new Timestamp(1645950039));

        transactionService.updateTransaction(transactionReceiveRequest);

        transaction.setStatus(TransactionStatusEnum.RECEIVE.getStatus());
        transaction.setTotalMoney(100.00);

        bankMoney.setTotalMoney(200.00);
        bankMoney.setUpdatedBy(1);
        bankMoney.setUpdatedAt(new Timestamp(1645950039));

        verify(transactionRepository).save(transaction);
        verify(bankMoneyRepository).save(bankMoney);
    }

    @Test
    @DisplayName("test case updateTransaction branch not found")
    void test_updateTransaction_branch_not_found() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);
        transactionService.setBankMoneyRepository(bankMoneyRepository);
        transactionService.setBankMoneyHistoryRepository(bankMoneyHistoryRepository);
        transactionService.setDateUtils(dateUtils);

        TransactionReceiveRequest transactionReceiveRequest = new TransactionReceiveRequest();
        transactionReceiveRequest.setTransactionId(1);
        transactionReceiveRequest.setReceiveBy(1);
        transactionReceiveRequest.setTotalMoney(100.00);
        transactionReceiveRequest.setReceiveBranchId(1);
        transactionReceiveRequest.setMoneyBoxId("1234");

        Transaction transaction = new Transaction();
        transaction.setId(1);
        transaction.setStatus(TransactionStatusEnum.CREATED.getStatus());

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.of(transaction));
        when(bankMoneyRepository.findByBranchId(anyInt())).thenReturn(Optional.empty());

        UpdateTransactionNotFoundException exception = assertThrows(UpdateTransactionNotFoundException.class, () -> transactionService.updateTransaction(transactionReceiveRequest));

        String expectedMessage = "Branch not found.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("test case updateTransaction transaction not found")
    void test_updateTransaction_transaction_not_found() {
        TransactionService transactionService = new TransactionService();
        transactionService.setTransactionRepository(transactionRepository);

        TransactionReceiveRequest transactionReceiveRequest = new TransactionReceiveRequest();
        transactionReceiveRequest.setTransactionId(1);
        transactionReceiveRequest.setReceiveBy(1);
        transactionReceiveRequest.setTotalMoney(100.00);
        transactionReceiveRequest.setReceiveBranchId(1);
        transactionReceiveRequest.setMoneyBoxId("1234");

        when(transactionRepository.findByIdAndMoneyBoxId(anyInt(), anyString())).thenReturn(Optional.empty());

        UpdateTransactionNotFoundException exception = assertThrows(UpdateTransactionNotFoundException.class, () -> transactionService.updateTransaction(transactionReceiveRequest));

        String expectedMessage = "Transaction not found.";
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