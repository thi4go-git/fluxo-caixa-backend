package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.rest.dto.DashboardDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoDTOResponse;
import com.dynss.cloudtecnologia.rest.dto.LancamentoDataDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoReflectionDTO;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LancamentoMapper {

    public LancamentoDataDTO listLancamentoToLancamentoDataDTO(List<Lancamento> lancamentos, String data_inicio, String data_fim) {
        LancamentoDataDTO lancamentoDataDTO = new LancamentoDataDTO();

        List<LancamentoDTOResponse> response = new ArrayList<>();
        for (Lancamento lancamento : lancamentos) {
            response.add(new LancamentoDTOResponse(lancamento));
        }
        lancamentoDataDTO.setData_inicio(LocalDate.parse(data_inicio));
        lancamentoDataDTO.setData_fim(LocalDate.parse(data_fim));
        lancamentoDataDTO.setTotal_lancamentos(response.size());
        lancamentoDataDTO.setLancamentos(response);

        return lancamentoDataDTO;
    }

    public DashboardDTO listLancamentoReflectionToDashboardDTO(List<LancamentoReflectionDTO> lancamentos,
                                                               BigDecimal sumEntradas, BigDecimal sumSaidas, Integer ano) {
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setLancamentos(lancamentos);
        dashboardDTO.setSumEntradas(sumEntradas);
        dashboardDTO.setSumSaidas(sumSaidas);
        dashboardDTO.setAno(ano);

        return dashboardDTO;

    }


}
