package com.example.kjavasystem.transaction.advice;

import com.example.kjavasystem.transaction.exception.CreateTransactionFailException;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TransactionControllerAdvice {
    @ExceptionHandler(CreateTransactionFailException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response createTransactionFail(CreateTransactionFailException e){
        return new Response(null, e.getMessage());
    }
}
