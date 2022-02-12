package com.playtomic.tests.wallet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class WalletServiceExceptionHandler {

    @ExceptionHandler(value = WalletNotFoundException.class)
    public ResponseEntity<String> walletNotFoundException(
            WalletNotFoundException walletNotFoundException) {

        return new ResponseEntity<>(
                walletNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = WalletAlreadyExistException.class)
    public ResponseEntity<String> walletAlreadyExistException(
            WalletAlreadyExistException walletAlreadyExistException) {

        return new ResponseEntity<>(
                walletAlreadyExistException.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = WalletTransactionFailedException.class)
    public ResponseEntity<String> walletTransactionFailedException(
            WalletTransactionFailedException walletTransactionFailedException) {

        return new ResponseEntity<>(
                walletTransactionFailedException.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    @ExceptionHandler(value = StripeAmountTooSmallException.class)
    public ResponseEntity<String> stripeAmountTooSmallException(
            StripeAmountTooSmallException stripeAmountTooSmallException) {

        return new ResponseEntity<>(
                stripeAmountTooSmallException.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = StripeServiceException.class)
    public ResponseEntity<String> stripeServiceException(
            StripeServiceException stripeServiceException) {

        return new ResponseEntity<>(
                stripeServiceException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
