package com.example.kjavasystem.management.service;

import com.example.kjavasystem.management.exception.CannotAccessDataException;
import com.example.kjavasystem.management.response.TotalMoneyResponse;
import com.example.kjavasystem.transaction.entity.BankMoney;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.repository.BankMoneyRepository;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.enums.RoleEnum;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
public class ManagementService {
    @Autowired
    BankMoneyRepository bankMoneyRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public void setBankMoneyRepository(BankMoneyRepository bankMoneyRepository) {
        this.bankMoneyRepository = bankMoneyRepository;
    }

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public TotalMoneyResponse getTotalMoneyFromBranch(int branchId, int employeeId) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent() && RoleEnum.MANAGER.getRoleId() == employee.get().getRoleId()) {
            Optional<BankMoney> bankMoney = bankMoneyRepository.findByBranchId(branchId);
            if (bankMoney.isPresent()) {
                TotalMoneyResponse totalMoneyResponse = new TotalMoneyResponse();
                totalMoneyResponse.setBranchId(bankMoney.get().getBranchId());
                totalMoneyResponse.setTotalMoney(bankMoney.get().getTotalMoney());
                return totalMoneyResponse;
            }
            throw new CannotAccessDataException("Cannot access data of this branch.");
        }

        throw new CannotAccessDataException("Your role cannot access data.");
    }
}
