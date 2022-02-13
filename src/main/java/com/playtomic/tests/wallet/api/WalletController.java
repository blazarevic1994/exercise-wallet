package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.entity.TransactionsEntity;
import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.service.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RequestMapping("/wallets")
@RestController
public class WalletController {

    @Autowired
    WalletService walletService;
    private Logger log = LoggerFactory.getLogger(WalletController.class);

    @PostMapping("")
    public ResponseEntity addNewWallet(@RequestBody WalletEntity walletEntity) {
        log.info("Add new wallet {}", walletEntity);
        WalletEntity savedWallet = walletService.addNewWallet(walletEntity);
        return new ResponseEntity<>(savedWallet, HttpStatus.CREATED);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity getWallet(@PathVariable String cardId) {
        log.info("Get wallet with id {}", cardId);
        WalletEntity walletEntity = walletService.getWallet(cardId);
        return new ResponseEntity<>(walletEntity, HttpStatus.OK);
    }

    @PutMapping("/top-up/{cardId}")
    public ResponseEntity topUpWallet(@PathVariable String cardId,
                                      @RequestBody BigDecimal amount) throws InterruptedException {
        log.info("Top up wallet {} for amount {}", cardId, amount);
        WalletEntity updatedWallet = walletService.topUpWallet(cardId, amount);
        return new ResponseEntity<>(updatedWallet, HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public ResponseEntity getAllTransactions() {
        log.info("List all transactions.");
        List<TransactionsEntity> transactionsEntities = walletService.getAllTransactions();
        return new ResponseEntity<>(transactionsEntities, HttpStatus.OK);
    }

    @GetMapping("/transactions/{cardId}")
    public ResponseEntity getAllTransactionsForCardId(@PathVariable String cardId) {
        log.info("List all transactions for card {}.", cardId);
        List<TransactionsEntity> transactionsEntities = walletService.getTransactionsForCardid(cardId);
        return new ResponseEntity<>(transactionsEntities, HttpStatus.OK);
    }

}
