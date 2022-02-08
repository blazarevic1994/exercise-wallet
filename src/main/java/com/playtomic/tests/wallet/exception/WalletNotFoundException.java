package com.playtomic.tests.wallet.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletNotFoundException extends RuntimeException {

    private String message;

    public WalletNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
