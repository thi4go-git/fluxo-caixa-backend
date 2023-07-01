package com.dynss.cloudtecnologia.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrosConstraintViolationDTO {
    List<String> erros;
}
