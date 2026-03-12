package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class PluggyTransactionsDTO extends PluggyPageDTO {
    private List<PluggyTransactionDTO> results;
}
