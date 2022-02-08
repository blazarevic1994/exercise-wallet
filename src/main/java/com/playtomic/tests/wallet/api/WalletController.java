package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping("/wallets")
@RestController
public class WalletController {
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @Autowired
    WalletService walletService;


    @PostMapping("/")
    public ResponseEntity addNewWallet(@RequestBody WalletEntity walletEntity) {
        WalletEntity savedWallet = walletService.addNewWallet(walletEntity);
        return new ResponseEntity<>(savedWallet, HttpStatus.CREATED);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity getWallet(@PathVariable String cardId) {
        WalletEntity walletEntity = walletService.getWallet(cardId);
        return new ResponseEntity<>(walletEntity, HttpStatus.OK);
    }

    @PutMapping("/top-up/{cardId}")
    public ResponseEntity topUpWallet(@PathVariable String cardId, @RequestBody BigDecimal amount) {
        WalletEntity updatedWallet = walletService.topUpWallet(cardId, amount);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }


}
