package com.playtomic.tests.wallet.repo;

import com.playtomic.tests.wallet.entity.WalletEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface WalletRepository extends MongoRepository<WalletEntity, String> {

}
