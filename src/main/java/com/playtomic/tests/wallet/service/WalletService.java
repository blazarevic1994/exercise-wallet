package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.entity.TransactionsEntity;
import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.exception.StripeServiceException;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.exception.WalletTransactionFailedException;
import com.playtomic.tests.wallet.repo.TransactionRepository;
import com.playtomic.tests.wallet.repo.WalletRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final StripeService stripeService;
    private final int maxTrials;
    private final int timeout;
    private Logger log = LoggerFactory.getLogger(WalletService.class);


    @Autowired
    public WalletService(WalletRepository walletRepository,
                         TransactionRepository transactionRepository,
                         StripeService stripeService,
                         @Value("${transaction.max.trials}") int maxTrials,
                         @Value("${transaction.timeout}") int timeout) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.stripeService = stripeService;
        this.maxTrials = maxTrials;
        this.timeout = timeout;
    }

    public WalletEntity addNewWallet(WalletEntity walletEntity)
            throws WalletAlreadyExistException {

        if (walletRepository.existsById(walletEntity.getCardId()))
            throw new WalletAlreadyExistException(
                    "Wallet with card ident " + walletEntity.getCardId() + " already exist!");
        return walletRepository.save(walletEntity);
    }

    public WalletEntity getWallet(String cardId) throws WalletNotFoundException {

        return findWallet(cardId);
    }

    public WalletEntity topUpWallet(String cardId, BigDecimal amount)
            throws WalletNotFoundException, StripeServiceException {

        WalletEntity walletEntity = findWallet(cardId);
        WalletEntity resultedEntity = null;

        StripeService.ChargeResponse chargeResponse = stripeService.charge(cardId, amount);

        TransactionsEntity transactionsEntity = new TransactionsEntity();
        transactionsEntity.setTransactionId(chargeResponse.getId());
        transactionsEntity.setCardId(cardId);
        transactionsEntity.setStatus(TransactionsEntity.Status.ACCEPTED);
        transactionsEntity.setAmount(chargeResponse.getAmount());
        transactionRepository.save(transactionsEntity);

        walletEntity.setBalance(walletEntity.getBalance().add(amount));
        boolean updatedWallet = false;
        int trials = 0;
        do {
            try {
                resultedEntity = walletRepository.save(walletEntity);
                updatedWallet = true;
            } catch (OptimisticLockingFailureException oexc) {
                try {
                    log.error("Optimistic lock failed:", oexc);
                    Thread.sleep(timeout);
                    walletEntity = findWallet(cardId);
                    walletEntity.setBalance(walletEntity.getBalance().add(amount));
                    log.warn("Transaction failed, try again.[{}]", trials);
                } catch (InterruptedException e) {
                    log.error("Exception during sleep:", e);
                }
            }
        } while (!updatedWallet && ++trials <= maxTrials);

        if (!updatedWallet) {
            // This is very bad situation. Transaction will have status Reject,
            // and some check service could detect it and try again.
            transactionsEntity.setStatus(TransactionsEntity.Status.REJECTED);
            transactionRepository.save(transactionsEntity);
            log.error("Transaction failed!");
            throw new WalletTransactionFailedException("Transaction failed!");
        }
        log.info("Transaction finised successfully!");
        return resultedEntity;
    }

    public List<TransactionsEntity> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<TransactionsEntity> getTransactionsForCardid(String cardId) {
        return transactionRepository.getAllByCardId(cardId);
    }


    private WalletEntity findWallet(String cardId) throws WalletNotFoundException {
        Optional<WalletEntity> walletEntity = walletRepository.findById(cardId);
        if (!walletEntity.isPresent())
            throw new WalletNotFoundException(
                    "Wallet with card ident " + cardId + " not found!");
        return walletEntity.get();
    }
}
