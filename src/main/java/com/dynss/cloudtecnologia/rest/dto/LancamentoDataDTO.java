package com.dynss.cloudtecnologia.rest.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class LancamentoDataDTO {
    LocalDate dataInicio;
    LocalDate dataFim;
    Integer totalLancamentos;
    List<LancamentoDTOResponse> lancamentos;
}
