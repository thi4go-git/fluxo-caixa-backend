package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;



@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
@AllArgsConstructor
public class PluggyPartyDTO {
    private String routingNumber;
    private String routingNumberISPB;
    private String branchNumber;
    private String accountNumber;
    private String name;
    private PluggyDocumentNumberDTO documentNumber;
}
