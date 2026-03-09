package com.dynss.cloudtecnologia.rest.dto.pluggy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Builder
public class PluggyAuthRequestDTO {
    private String clientId;
    private String clientSecret;
}
