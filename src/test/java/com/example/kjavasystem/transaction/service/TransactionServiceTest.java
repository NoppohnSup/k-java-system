package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.repository.TransactionRepository;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepository;

    @Test
    @DisplayName("test case create transaction success")
    void test_createTransaction_success() {
        TransactionService transactionService = new TransactionService(transactionRepository);
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
        TransactionService transactionService = new TransactionService(transactionRepository);
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setCratedBy(1);

        doThrow(new RuntimeException("error")).when(transactionRepository).findFirstByOrderByIdDesc();

        CreateTransactionFailException exception = assertThrows(CreateTransactionFailException.class, () -> transactionService.createTransaction(transactionRequest));

        String expectedMessage = "error";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}