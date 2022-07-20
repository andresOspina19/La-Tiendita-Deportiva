package com.misiontic.payments_ms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
@ResponseBody
public class PaymentNotValidAdvice {
    @ResponseBody
    @ExceptionHandler(PaymentNotValidException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    String NoStockAdvice(PaymentNotValidException ex){
        return ex.getMessage();
    }
}
