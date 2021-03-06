package com.playtomic.tests.wallet.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {

    // Card identifier. It should be unique.
    @Id
    @NonNull
    @JsonProperty("card_id")
    String cardId;

    // Current balance.
    @NonNull
    @JsonProperty("balance")
    BigDecimal balance;

    // Timestamp on last balance update.
    @NonNull
    @JsonProperty("created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    // Use this for providing optimistic locking mechanism.
    // Basically, we need to take care about potential race condition.
    @JsonIgnore
    @Version
    Long version;

}
