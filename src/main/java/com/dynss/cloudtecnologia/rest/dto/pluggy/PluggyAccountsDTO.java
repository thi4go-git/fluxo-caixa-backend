package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Getter
@Setter
public class PluggyAccountsDTO extends PluggyPageDTO {
    private List<PluggyAccountDTO> results;
}
