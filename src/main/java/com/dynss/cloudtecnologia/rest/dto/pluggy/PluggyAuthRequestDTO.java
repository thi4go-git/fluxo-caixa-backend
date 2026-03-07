package com.dynss.cloudtecnologia.rest.dto.pluggy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PluggyAuthRequestDTO {
    private String clientId;
    private String clientSecret;
}
