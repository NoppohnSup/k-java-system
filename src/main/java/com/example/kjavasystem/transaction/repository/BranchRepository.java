package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Integer> {
}
