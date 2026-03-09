package com.dynss.cloudtecnologia.rest.dto.pluggy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PluggyWebhookErrorDTO {
    private String code;
    private String message;
}
