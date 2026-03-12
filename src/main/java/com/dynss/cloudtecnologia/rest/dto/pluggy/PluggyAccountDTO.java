package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyAccountDTO {
    private String id;
    private String type;
    private String subtype;
    private String name;
    private BigDecimal balance;
    private String currencyCode;
    private String itemId;
    private String number;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String marketingName;
    private String taxNumber;
    private String owner;
    private PluggyBankDataDTO bankData;
    private PluggyCreditDataDTO creditData;
}
