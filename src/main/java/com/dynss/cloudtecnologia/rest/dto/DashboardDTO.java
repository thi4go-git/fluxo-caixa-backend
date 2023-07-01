package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTO {
    List<LancamentoReflectionDTO> lancamentos;
    BigDecimal sumEntradas, sumSaidas;
    Integer ano;
}
