package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.Branch;
import com.example.kjavasystem.transaction.entity.Employee;
import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.exception.RoleCannotAccessException;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.BranchRepository;
import com.example.kjavasystem.transaction.repository.EmployeeRepository;
import com.example.kjavasystem.transaction.repository.TransactionRepository;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.utils.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    public TransactionService() {
    }

    public void setTransactionRepository(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public void setBranchRepository(BranchRepository branchRepository) {
        this.branchRepository = branchRepository;
    }

    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest){
        try {
            String moneyBoxId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction();
            transaction.setMoneyBoxId(moneyBoxId);
            transaction.setTotalMoney(transactionRequest.getTotalMoney());
            transaction.setStatus(transactionRequest.getStatus());
            transaction.setSenderBranchId(transactionRequest.getSenderBranchId());
            transaction.setReceiveBranchId(transactionRequest.getReceiveBranchId());
            transaction.setCreatedBy(transactionRequest.getCratedBy());
            transactionRepository.save(transaction);

            Transaction lastTransaction = transactionRepository.findFirstByOrderByIdDesc();

            TransactionResponse transactionResponse = new TransactionResponse();
            transactionResponse.setTransactionId(lastTransaction.getId());
            transactionResponse.setMoneyBoxId(moneyBoxId);

            return transactionResponse;
        } catch (Exception e) {
            throw new CreateTransactionFailException(e.getMessage());
        }
    }

    public TransactionDto getTransaction(Integer id, String moneyBoxId, int employeeId) {
        Optional<Transaction> transaction = transactionRepository.findByIdAndMoneyBoxId(id, moneyBoxId);
        if (transaction.isPresent()) {
            Optional<Employee> employee = employeeRepository.findById(employeeId);
            if (employee.isPresent()) {
                Optional<Branch> receiverBranch = branchRepository.findById(transaction.get().getReceiveBranchId());
                Optional<Branch> senderBranch = branchRepository.findById(transaction.get().getSenderBranchId());
                TransactionDto transactionDto = new TransactionDto();

                if (RoleEnum.TRANSPORT_STAFF.getRoleId() == employee.get().getRoleId()) {
                    transactionDto.setStatus(transaction.get().getStatus());
                    transactionDto.setId(transaction.get().getId());
                    transactionDto.setMoneyBoxId(transaction.get().getMoneyBoxId());
                    transactionDto.setReceiverBranch(receiverBranch.get().getName());
                    transactionDto.setSenderBranch(senderBranch.get().getName());
                } else if (RoleEnum.CASH_CENTER_STAFF.getRoleId() == employee.get().getRoleId()) {
                    transactionDto.setId(transaction.get().getId());
                    transactionDto.setMoneyBoxId(transaction.get().getMoneyBoxId());
                    transactionDto.setTotalMoney(transaction.get().getTotalMoney());
                    transactionDto.setStatus(transaction.get().getStatus());
                    transactionDto.setReceiverBranch(receiverBranch.get().getName());
                    transactionDto.setSenderBranch(senderBranch.get().getName());
                } else {
                    throw new RoleCannotAccessException("Your role cannot access data.");
                }

                return transactionDto;
            }
            throw new TransactionNotFoundException("Employee not found.");
        }
        throw new TransactionNotFoundException("Transaction not found.");
    }
}
