package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.BankMoney;
import com.example.kjavasystem.transaction.entity.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BankMoneyRepositoryTest {
    @Autowired
    BankMoneyRepository bankMoneyRepository;

    @Test
    @DisplayName("repository test case findByBranchId.")
    void test_findByBranchId_success() {
        BankMoney bankMoney = new BankMoney();
        bankMoney.setId(1);
        bankMoney.setBranchId(1);
        bankMoney.setTotalMoney(100.00);
        bankMoney.setUpdatedBy(1);
        bankMoneyRepository.save(bankMoney);

        Optional<BankMoney> actual = bankMoneyRepository.findByBranchId(1);
        assertTrue(actual.isPresent());
    }
}