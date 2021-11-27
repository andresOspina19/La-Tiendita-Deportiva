package com.latienditadeportiva.Inventory_ms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@ResponseBody
public class NoStockAdvice {
    @ResponseBody
    @ExceptionHandler(NoStockException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String NoStockAdvice(NoStockException ex){
        return ex.getMessage();
    }
}