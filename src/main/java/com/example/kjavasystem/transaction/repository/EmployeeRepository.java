package com.example.kjavasystem.transaction.repository;

import com.example.kjavasystem.transaction.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
