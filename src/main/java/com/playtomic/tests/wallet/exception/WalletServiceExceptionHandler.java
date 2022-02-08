package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletServiceExceptionHandler {

    @ExceptionHandler(value = WalletNotFoundException.class)
    public ResponseEntity walletNotFoundException(WalletNotFoundException walletNotFoundException) {
        return new ResponseEntity(walletNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = WalletAlreadyExistException.class)
    public ResponseEntity walletAlreadyExistException(WalletAlreadyExistException walletAlreadyExistException) {
        return new ResponseEntity(walletAlreadyExistException.getMessage(), HttpStatus.CONFLICT);
    }
}
