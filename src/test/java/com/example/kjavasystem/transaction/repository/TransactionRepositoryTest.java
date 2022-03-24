package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TransactionRepositoryTest {
    @Autowired
    TransactionRepository transactionRepository;

    @Test
    @DisplayName("repository test case findFirstByOrderByIdDesc.")
    void test_findFirstByOrderByIdDesc_success() {
        Transaction transaction = new Transaction();
        transaction.setId(1);
        transactionRepository.save(transaction);

        Transaction actual = transactionRepository.findFirstByOrderByIdDesc();
        assertEquals(1, actual.getId());
    }

    @Test
    @DisplayName("repository test case findByIdAndMoneyBoxId.")
    void test_findByIdAndMoneyBoxId_success() {
        Transaction transaction = new Transaction();
        transaction.setId(2);
        transaction.setMoneyBoxId("1234");
        transactionRepository.save(transaction);

        Optional<Transaction> actual = transactionRepository.findByIdAndMoneyBoxId(2, "1234");
        assertTrue(actual.isPresent());
    }
}