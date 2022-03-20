package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.*;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.exception.RoleCannotAccessException;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.exception.UpdateTransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.*;
import com.example.kjavasystem.transaction.request.TransactionReceiveRequest;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.utils.DateUtils;
import com.example.kjavasystem.utils.enums.RoleEnum;
import com.example.kjavasystem.utils.enums.TransactionStatusEnum;
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

    @Autowired
    BankMoneyRepository bankMoneyRepository;

    @Autowired
    BankMoneyHistoryRepository bankMoneyHistoryRepository;

    @Autowired
    DateUtils dateUtils;

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

    public void setBankMoneyRepository(BankMoneyRepository bankMoneyRepository) {
        this.bankMoneyRepository = bankMoneyRepository;
    }

    public void setDateUtils(DateUtils dateUtils) {
        this.dateUtils = dateUtils;
    }

    public void setBankMoneyHistoryRepository(BankMoneyHistoryRepository bankMoneyHistoryRepository) {
        this.bankMoneyHistoryRepository = bankMoneyHistoryRepository;
    }

    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest){
        try {
            String moneyBoxId = UUID.randomUUID().toString();
            Transaction transaction = new Transaction();
            transaction.setMoneyBoxId(moneyBoxId);
            transaction.setTotalMoney(transactionRequest.getTotalMoney());
            transaction.setStatus(TransactionStatusEnum.CREATED.getStatus());
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

    @Transactional
    public void updateTransaction(TransactionReceiveRequest transactionRequest){
        Optional<Transaction> transaction = transactionRepository.findByIdAndMoneyBoxId(transactionRequest.getTransactionId(), transactionRequest.getMoneyBoxId());

        if (transaction.isPresent() && TransactionStatusEnum.CREATED.getStatus().equals(transaction.get().getStatus())) {
            transaction.get().setTotalMoney(transactionRequest.getTotalMoney());
            transaction.get().setStatus(TransactionStatusEnum.RECEIVE.getStatus());

            transactionRepository.save(transaction.get());

            Optional<BankMoney> bankMoney = bankMoneyRepository.findByBranchId(transactionRequest.getReceiveBranchId());

            if (bankMoney.isPresent()) {
                BankMoneyHistory bankMoneyHistory = new BankMoneyHistory();
                bankMoneyHistory.setBankMoneyId(bankMoney.get().getId());
                bankMoneyHistory.setTransactionId(transactionRequest.getTransactionId());
                bankMoneyHistory.setBranchId(bankMoney.get().getBranchId());
                bankMoneyHistory.setTotalMoney(bankMoney.get().getTotalMoney());
                bankMoneyHistory.setUpdatedBy(bankMoney.get().getUpdatedBy());
                bankMoneyHistory.setCreatedAt(bankMoney.get().getCreatedAt());
                bankMoneyHistory.setUpdatedAt(bankMoney.get().getUpdatedAt());
                bankMoneyHistoryRepository.save(bankMoneyHistory);

                double totalMoneyInBank = bankMoney.get().getTotalMoney();
                bankMoney.get().setTotalMoney(totalMoneyInBank + transactionRequest.getTotalMoney());
                bankMoney.get().setUpdatedAt(dateUtils.getCurrentDate());
                bankMoney.get().setUpdatedBy(transactionRequest.getReceiveBy());
                bankMoneyRepository.save(bankMoney.get());
            } else {
                throw new UpdateTransactionNotFoundException("Branch not found.");
            }
        } else {
            throw new UpdateTransactionNotFoundException("Transaction not found.");
        }
    }
}
