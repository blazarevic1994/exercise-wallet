package com.playtomic.tests.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {

    // Card identifier. It should be unique.
    @Id
    String cardId;

    // Current balance.
    BigDecimal balance;

    // Timestamp on last balance update.
    LocalDateTime lastUpdate = LocalDateTime.now();

    // Use this for optimistic locking mechanism.
    // Basically, we need to take care about potential race condition.
    @JsonIgnore
    @Version
    Long version;

}
