package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findFirstByOrderByIdDesc();

    Optional<Transaction> findByIdAndMoneyBoxId(int id, String moneyBoxId);
}
