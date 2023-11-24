package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.*;
import java.util.List;

public interface LancamentoService {

    LancamentoNewDTO lancar(LancamentoNewDTO dto);


    LancamentoDataDTO listarLancamentosByUsuarioDate(String username,
                                                     String dataInicio, String dataFim);

    LancamentoDataDTO listarLancamentosByUsuarioDateFilter(LancamentoFilterDTO dtoFilter);

    List<Situacao> listarSituacao();

    List<TipoLancamento> listarTipoLancamento();

    DashboardDTO getLancamentosDashboard(String username);

    void deleteById(Long id);

    void deleteByIdList(List<String> idsDeletar);

    LancamentoUpdateDTO update(LancamentoUpdateDTO dto);

    List<Lancamento> lancamentosUsuarioPorNatureza(final String username, Long idNatureza);

    void uploadAnexo(AnexoUploaDTO anexoUploaDTO, Long idLancamento);

    Lancamento findByIdOrThrow(Long idLancamento);
}
