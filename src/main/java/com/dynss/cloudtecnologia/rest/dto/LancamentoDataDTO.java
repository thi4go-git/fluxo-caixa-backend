package com.dynss.cloudtecnologia.rest.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
public class LancamentoDataDTO {
    private LocalDate data_inicio;
    private LocalDate data_fim;
    private Integer total_lancamentos;
    private List<LancamentoDTOResponse> lancamentos;
}
