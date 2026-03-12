package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyCreditDataDTO {
    private String level;
    private String brand;
    private LocalDate balanceCloseDate;
    private LocalDate balanceDueDate;
    private BigDecimal availableCreditLimit;
    private BigDecimal balanceForeignCurrency;
    private BigDecimal minimumPayment;
    private BigDecimal creditLimit;
    private Boolean isLimitFlexible;
    private String holderType;
    private String status;
    private List<PluggyCreditLimitDTO> disaggregatedCreditLimits;
    private List<PluggyAdditionalCardDTO> additionalCards;
}
