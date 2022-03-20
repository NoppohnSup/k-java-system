package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    Transaction findFirstByOrderByIdDesc();
}
