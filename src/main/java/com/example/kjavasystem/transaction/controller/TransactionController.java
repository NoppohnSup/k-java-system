package com.example.kjavasystem.transaction.controller;

import com.example.kjavasystem.transaction.dto.TransactionDto;
import com.example.kjavasystem.transaction.entity.Transaction;
import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.transaction.exception.TransactionNotFoundException;
import com.example.kjavasystem.transaction.repository.TransactionRepository;
import com.example.kjavasystem.transaction.request.TransactionReceiveRequest;
import com.example.kjavasystem.transaction.request.TransactionRequest;
import com.example.kjavasystem.transaction.response.TransactionResponse;
import com.example.kjavasystem.transaction.service.TransactionService;
import com.example.kjavasystem.utils.enums.MessageResponseEnum;
import com.example.kjavasystem.utils.model.Response;
import org.hibernate.query.QueryParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.Optional;

@RestController
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping(value = "/transaction", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response createTransaction(@RequestBody TransactionRequest transactionRequest) {
        TransactionResponse transactionResponse = transactionService.createTransaction(transactionRequest);
        return new Response(transactionResponse, MessageResponseEnum.SUCCESS.getMessage());
    }

    @GetMapping(value = "/transaction/{id}")
    public Response getTransaction(@PathVariable Integer id, @RequestParam String moneyBoxId, @RequestParam Integer employeeId){
        TransactionDto transaction = transactionService.getTransaction(id, moneyBoxId, employeeId);
        return new Response(transaction, MessageResponseEnum.SUCCESS.getMessage());
    }

    @PostMapping(value = "/transaction/update", produces = MediaType.APPLICATION_JSON_VALUE)
    public Response updateTransaction(@RequestBody TransactionReceiveRequest transactionRequest) {
        transactionService.updateTransaction(transactionRequest);
        return new Response("", MessageResponseEnum.SUCCESS.getMessage());
    }
}
