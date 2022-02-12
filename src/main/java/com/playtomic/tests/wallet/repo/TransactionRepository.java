package com.playtomic.tests.wallet.repo;

import com.playtomic.tests.wallet.entity.TransactionsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface TransactionRepository extends MongoRepository<TransactionsEntity, String> {

    List<TransactionsEntity> getAllByCardId(String cardId);

}
