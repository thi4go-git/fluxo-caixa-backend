package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
public class DashboardDTO {
    List<LancamentoReflectionDTO> lancamentos;
    long sumEntradas;
    long sumSaidas;
    Integer ano;
}
