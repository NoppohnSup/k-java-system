package com.example.kjavasystem.transaction.service;

import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.repository.TransactionRepository;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
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
}
