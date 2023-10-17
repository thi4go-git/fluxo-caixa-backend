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

    public LancamentoDataDTO listLancamentoToLancamentoDataDTO(List<Lancamento> lancamentos, String data_inicio, String data_fim) {
        LancamentoDataDTO lancamentoDataDTO = new LancamentoDataDTO();

        List<LancamentoDTOResponse> lancamentoDTOResponses =
                lancamentos.stream().map(this::lancamenToLancamentoDTOResponse).collect(Collectors.toList());

        lancamentoDataDTO.setData_inicio(LocalDate.parse(data_inicio));
        lancamentoDataDTO.setData_fim(LocalDate.parse(data_fim));
        lancamentoDataDTO.setTotal_lancamentos(lancamentoDTOResponses.size());
        lancamentoDataDTO.setLancamentos(lancamentoDTOResponses);

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


    public Lancamento newLancamentoCreate(LancamentoDTO dto, Integer nr_parcela, Usuario user,
                                          BigDecimal valor_parcela, LocalDate data_lancamento, Natureza natureza) {
        Lancamento lancamento = new Lancamento();
        lancamento.setTipo(dto.getTipo());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setData_lancamento(data_lancamento);
        lancamento.setValor_parcela(valor_parcela);
        lancamento.setQtde_parcelas(dto.getQtde_parcelas());
        lancamento.setNr_parcela(nr_parcela);
        lancamento.setNatureza(natureza);
        lancamento.setUsuario(user);
        lancamento.setSituacao(Situacao.EM_ABERTO);
        lancamento.setData_criacao(LocalDate.now());

        return lancamento;
    }

    public LancamentoDTOResponse lancamenToLancamentoDTOResponse(Lancamento lancamento) {
        LancamentoDTOResponse dto = ModelMapperConfig.getModelMapper().map(lancamento, LancamentoDTOResponse.class);
        if (Objects.nonNull(lancamento.getAnexo())) {
            dto.setNomeAnexo(lancamento.getAnexo().getNome());
        }

        return dto;
    }


}
