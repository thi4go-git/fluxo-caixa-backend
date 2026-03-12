package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyBankDataDTO {
    private String transferNumber;
    private BigDecimal closingBalance;
    private BigDecimal automaticallyInvestedBalance;
    private BigDecimal overdraftContractedLimit;
    private BigDecimal overdraftUsedLimit;
    private BigDecimal unarrangedOverdraftAmount;
}
