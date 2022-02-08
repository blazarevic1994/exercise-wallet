package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.entity.WalletEntity;
import com.playtomic.tests.wallet.exception.WalletAlreadyExistException;
import com.playtomic.tests.wallet.exception.WalletNotFoundException;
import com.playtomic.tests.wallet.repo.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {


    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public WalletEntity addNewWallet(WalletEntity walletEntity) throws WalletAlreadyExistException {
        if (walletRepository.existsById(walletEntity.getCardId()))
            throw new WalletAlreadyExistException("Wallet with card ident " + walletEntity.getCardId() + " already exist!");
        return walletRepository.save(walletEntity);
    }

    public WalletEntity getWallet(String cardId) throws WalletNotFoundException {
        return findWallet(cardId);
    }

    public WalletEntity topUpWallet(String cardId, BigDecimal amount) throws WalletNotFoundException {
        WalletEntity walletEntity = findWallet(cardId);
        walletEntity.setBalance(walletEntity.getBalance().add(amount));
        return walletRepository.save(walletEntity);
    }
    private WalletEntity findWallet(String cardId) throws WalletNotFoundException{
        Optional<WalletEntity> walletEntity = walletRepository.findById(cardId);
        if (!walletEntity.isPresent())
            throw new WalletNotFoundException("Wallet with card ident " + cardId + " not found!");
        return walletEntity.get();
    }
}
