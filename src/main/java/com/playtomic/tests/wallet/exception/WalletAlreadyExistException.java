package com.playtomic.tests.wallet.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletAlreadyExistException extends RuntimeException {

    private String message;

    public WalletAlreadyExistException(String message) {
        super(message);
        this.message = message;
    }
}
