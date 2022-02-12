package com.playtomic.tests.wallet.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsEntity {


    // Transaction id got from Stripe remote service.
    @Id
    @NonNull
    @JsonProperty("transaction_id")
    String transactionId;

    // Card identifier.
    @NonNull
    @JsonProperty("card_id")
    String cardId;

    // Current balance.
    @NonNull
    @JsonProperty("amount")
    BigDecimal amount;

    // Timestamp on last balance update.
    @NonNull
    @JsonProperty("created_at")
    LocalDateTime createdAt = LocalDateTime.now();

    // Transaction status.
    @NonNull
    @JsonProperty("status")
    Status status;

    public enum Status {
        ACCEPTED,
        REJECTED
    }

}
