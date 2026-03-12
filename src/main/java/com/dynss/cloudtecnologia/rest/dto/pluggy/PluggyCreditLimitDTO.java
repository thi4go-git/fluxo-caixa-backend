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
public class PluggyCreditLimitDTO {
    private String lineName;
    private BigDecimal usedAmount;
    private BigDecimal limitAmount;
    private BigDecimal availableAmount;
    private Boolean isLimitFlexible;
    private String consolidationType;
    private String creditLineLimitType;
    private String identificationNumber;
    private String lineNameAdditionalInfo;
    private String usedAmountCurrencyCode;
    private String limitAmountCurrencyCode;
    private String availableAmountCurrencyCode;
}
