package com.dynss.cloudtecnologia.rest.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class LancamentoDataDTO {
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private Integer totalLancamentos;
    private List<LancamentoDTOResponse> lancamentos;
}
