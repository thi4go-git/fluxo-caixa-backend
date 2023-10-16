package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.*;


import java.util.List;

public interface LancamentoService {

    LancamentoDTO lancar(LancamentoDTO dto);


    LancamentoDataDTO listarLancamentosByUsuarioDate(String username,
                                                     String data_inicio, String data_fim);

    LancamentoDataDTO listarLancamentosByUsuarioDateFilter(LancamentoFilterDTO dtoFilter);

    List<Situacao> listarSituacao();

    List<TipoLancamento> listarTipoLancamento();

    DashboardDTO getLancamentosDashboard(String username);

    void deleteById(Long id);

    void deleteByIdList(List<String> idsDeletar);

    LancamentoDTO update(LancamentoDTO dto);

    List<Lancamento> lancamentosUsuarioPorNatureza(final String username, Long idNatureza);


}
