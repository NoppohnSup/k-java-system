package com.example.kjavasystem.management.advice;

import com.example.kjavasystem.management.exception.CannotAccessDataException;
import com.example.kjavasystem.utils.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ManagementControllerAdvice {
    @ExceptionHandler(CannotAccessDataException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response cannotAccessData(CannotAccessDataException e){
        return new Response(null, e.getMessage());
    }
}
