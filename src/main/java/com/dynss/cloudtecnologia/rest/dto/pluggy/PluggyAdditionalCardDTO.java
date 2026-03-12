package com.dynss.cloudtecnologia.rest.dto.pluggy;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class PluggyAdditionalCardDTO {
    private String number;
}
