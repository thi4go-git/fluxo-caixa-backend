package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.config.ModelMapperConfig;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.rest.dto.*;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@ApplicationScoped
@Slf4j
public class LancamentoMapper {

    public LancamentoDataDTO listLancamentoToLancamentoDataDTO(List<Lancamento> lancamentos, String dataInicio, String dataFim) {
        LancamentoDataDTO lancamentoDataDTO = new LancamentoDataDTO();

        List<LancamentoDTOResponse> lancamentoDTOResponses = lancamentos.stream().map(this::lancamenToLancamentoDTOResponse).collect(Collectors.toList());

        lancamentoDataDTO.setDataInicio(LocalDate.parse(dataInicio));
        lancamentoDataDTO.setDataFim(LocalDate.parse(dataFim));
        lancamentoDataDTO.setTotalLancamentos(lancamentoDTOResponses.size());
        lancamentoDataDTO.setLancamentos(lancamentoDTOResponses);

        return lancamentoDataDTO;
    }

    public DashboardDTO listLancamentoReflectionToDashboardDTO(List<LancamentoReflectionDTO> lancamentos, long sumEntradas, long sumSaidas, Integer ano) {
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setLancamentos(lancamentos);
        dashboardDTO.setSumEntradas(sumEntradas);
        dashboardDTO.setSumSaidas(sumSaidas);
        dashboardDTO.setAno(ano);

        return dashboardDTO;

    }


    public Lancamento newLancamentoCreate(LancamentoNewDTO dto, Integer nrParcela, Usuario user, BigDecimal valorParcela, LocalDate dataLancamento, Natureza natureza) {
        Lancamento lancamento = new Lancamento();
        lancamento.setTipo(dto.getTipo());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setDataLancamento(dataLancamento);
        lancamento.setValorParcela(valorParcela);
        lancamento.setQtdeParcelas(dto.getQtdeParcelas());
        lancamento.setNrParcela(nrParcela);
        lancamento.setNatureza(natureza);
        lancamento.setUsuario(user);
        lancamento.setSituacao(Situacao.EM_ABERTO);
        lancamento.setDataCriacao(LocalDate.now());

        return lancamento;
    }

    public LancamentoDTOResponse lancamenToLancamentoDTOResponse(Lancamento lancamento) {
        LancamentoDTOResponse dto = ModelMapperConfig.getModelMapper().map(lancamento, LancamentoDTOResponse.class);
        if (Objects.nonNull(lancamento.getAnexo())) {
            dto.setNomeAnexo(lancamento.getAnexo().getNome());
        }
        dto.setNatureza(lancamento.getNatureza().getDescricao());

        return dto;
    }


}
