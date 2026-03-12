package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
public class PluggyPaymentDataDTO {
    private String reason;
    private String receiverReferenceId;
    private String referenceNumber;
    private String paymentMethod;
    private PluggyPartyDTO payer;
    private PluggyPartyDTO receiver;
    private Object boletoMetadata;
}
