package com.playtomic.tests.wallet.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletTransactionFailedException extends RuntimeException {

    private String message;

    public WalletTransactionFailedException(String message) {
        super(message);
        this.message = message;
    }
}
