package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.exception.LancamentoNaoEncontradoException;
import com.dynss.cloudtecnologia.exception.NaturezaNaoEncontrada;
import com.dynss.cloudtecnologia.exception.UsuarioNaoEncontradoException;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.model.repository.LancamentoRepository;
import com.dynss.cloudtecnologia.rest.dto.*;
import com.dynss.cloudtecnologia.rest.mapper.LancamentoMapper;
import com.dynss.cloudtecnologia.service.LancamentoService;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;


@ApplicationScoped
public class LancamentoServiceImpl implements LancamentoService {

    @Inject
    private LancamentoRepository lancamentoRepository;

    @Inject
    private UsuarioServiceImpl usuarioService;

    @Inject
    private NaturezaServiceImpl naturezaService;

    @Inject
    private LancamentoMapper lancamentoMapper;

    @Override
    @Transactional
    public LancamentoDTO lancar(LancamentoDTO dto) {

        Usuario usuario = usuarioService.findByUsername(dto.getUsername());
        if (usuario != null) {
            Natureza natureza = naturezaService
                    .findById(dto.getId_natureza());
            if (natureza != null) {
                if (dto.getTipo() == TipoLancamento.DEBITO) {
                    dto.setValor_total(dto.getValor_total().negate());
                }
                BigDecimal vlrParcelas = dto.getValor_total().divide(
                        new BigDecimal(dto.getQtde_parcelas()), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN);
                for (int index = 1; index <= dto.getQtde_parcelas(); index++) {
                    int parcela = index;
                    LocalDate data_lancamento;
                    if (parcela == 1) {
                        data_lancamento = dto.getData_referencia();
                        Lancamento lancamento = new Lancamento(dto, parcela, usuario, vlrParcelas, data_lancamento, natureza);
                        lancamentoRepository.persist(lancamento);
                    } else {
                        data_lancamento = dto.getData_referencia().plusMonths(parcela - 1);
                        Lancamento lancamento = new Lancamento(dto, parcela, usuario, vlrParcelas, data_lancamento, natureza);
                        lancamentoRepository.persist(lancamento);
                    }
                }
                return dto;
            }
            throw new NaturezaNaoEncontrada();
        }
        throw new UsuarioNaoEncontradoException();
    }


    @Override
    public LancamentoDataDTO listarLancamentosByUsuarioDate(String username, String data_inicio, String data_fim) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        List<Lancamento> lancamentosDate = lancamentoRepository
                .listarLancamentosByUsuarioDate(usuario, data_inicio, data_fim);

        return lancamentoMapper.listLancamentoToLancamentoDataDTO(lancamentosDate, data_inicio, data_fim);
    }

    @Override
    public LancamentoDataDTO listarLancamentosByUsuarioDateFilter(LancamentoFilterDTO dtoFilter) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dtoFilter.getUsername());
        List<Lancamento> lancamentosDateFilter = lancamentoRepository
                .listarLancamentosByUsuarioDateFilter(usuario, dtoFilter);

        return lancamentoMapper.listLancamentoToLancamentoDataDTO(lancamentosDateFilter, dtoFilter.getData_inicio(), dtoFilter.getData_fim());
    }


    @Override
    public List<Situacao> listarSituacao() {
        return List.of(Situacao.values());
    }

    @Override
    public List<TipoLancamento> listarTipoLancamento() {
        return List.of(TipoLancamento.values());
    }

    @Override
    public DashboardDTO getLancamentosDashboard(String username) {
        if (username != null) {
            Usuario usuario = usuarioService.findByUsername(username);
            if (usuario != null) {
                List<LancamentoReflectionDTO> lancamentos = lancamentoRepository
                        .getLancamentosDashboard(usuario);
                BigDecimal sumEntradas = new BigDecimal(0);
                BigDecimal sumSaidas = new BigDecimal(0);
                Integer ano = LocalDate.now().getYear();
                for (LancamentoReflectionDTO refle : lancamentos) {
                    sumEntradas = sumEntradas.add(refle.getSaldo_entradas());
                    sumSaidas = sumSaidas.add(refle.getSaldo_saidas());
                }
                DashboardDTO dashboardDTO = lancamentoMapper
                        .listLancamentoReflectionToDashboardDTO(lancamentos, sumEntradas, sumSaidas, ano);
                return dashboardDTO;
            }
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Lancamento lancamento = lancamentoRepository.findById(id);
        if (lancamento != null) {
            lancamentoRepository.deleteById(lancamento.getId());
        } else {
            throw new LancamentoNaoEncontradoException();
        }
    }

    @Override
    @Transactional
    public LancamentoDTO update(LancamentoDTO dto) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dto.getUsername());
        Natureza natureza = naturezaService.getNaturezaByUsuarioAndIDOrThrow(usuario, dto.getId_natureza());
        Lancamento lancamento = lancamentoRepository.findByIdAndUsuarioOrThrow(usuario, dto.getId());

        if (dto.getTipo() == TipoLancamento.DEBITO) {
            dto.setValor_total(dto.getValor_total().negate());
        }
        lancamento.setTipo(dto.getTipo());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setData_lancamento(dto.getData_referencia());
        lancamento.setValor_parcela(dto.getValor_total());
        lancamento.setNatureza(natureza);

        lancamentoRepository.persist(lancamento);

        return dto;
    }


}
