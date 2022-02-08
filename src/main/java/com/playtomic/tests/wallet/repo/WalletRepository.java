package com.playtomic.tests.wallet.repo;

import com.playtomic.tests.wallet.entity.WalletEntity;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface WalletRepository extends MongoRepository<WalletEntity, String> {

}
