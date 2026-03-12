package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
public class PluggyTransactionDTO {
    private String id;
    private OffsetDateTime date;
    private BigDecimal amount;
    private String providerCode;
    private String descriptionRaw;
    private String description;
    private String type;
    private String operationType;
    private String category;
    private String categoryId;
    private String currencyCode;
    private String status;
    private String accountId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private Integer order;
    private String providerId;
    private BigDecimal amountInAccountCurrency;
    private PluggyMerchantDTO merchant;
    private PluggyPaymentDataDTO paymentData;
    private Object acquirerData;
    private Object balance;
    private Object creditCardMetadata;
    private PluggyPartyDTO payer;
}
