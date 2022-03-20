package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.BankMoneyHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankMoneyHistoryRepository extends JpaRepository<BankMoneyHistory, Integer> {
}
