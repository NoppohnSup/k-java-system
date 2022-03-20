package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.BankMoney;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BankMoneyRepository extends JpaRepository<BankMoney, Integer> {
    Optional<BankMoney> findByBranchId(int branchId);
}
