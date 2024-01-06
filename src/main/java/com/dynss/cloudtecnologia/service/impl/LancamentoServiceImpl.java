package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.model.entity.Anexo;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.enums.Origem;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.model.repository.LancamentoRepository;
import com.dynss.cloudtecnologia.rest.dto.*;
import com.dynss.cloudtecnologia.rest.mapper.LancamentoMapper;
import com.dynss.cloudtecnologia.service.LancamentoService;
import com.dynss.cloudtecnologia.utils.FilesUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;


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

    @Inject
    private AnexoServiceImpl anexoService;


    @Override
    @Transactional
    public LancamentoNewDTO lancar(LancamentoNewDTO dto) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dto.getUsername());
        Natureza natureza = naturezaService.findByIdOrThrow(dto.getIdNatureza());

        return this.processarLancamento(usuario, natureza, dto);
    }

    private LancamentoNewDTO processarLancamento(Usuario usuario, Natureza natureza, LancamentoNewDTO dto) {
        if (dto.getTipo().equals(TipoLancamento.AMBOS)) {
            for (int vezesLancar = 1; vezesLancar <= 2; vezesLancar++) {
                if (vezesLancar == 1) {
                    dto.setTipo(TipoLancamento.CREDITO);
                } else {
                    dto.setTipo(TipoLancamento.DEBITO);
                }
                efetuarLancamento(usuario, natureza, dto);
            }
        } else {
            efetuarLancamento(usuario, natureza, dto);
        }
        return dto;
    }

    private void efetuarLancamento(Usuario usuario, Natureza natureza, LancamentoNewDTO lancamentoNewDTO) {
        if (lancamentoNewDTO.getTipo() == TipoLancamento.DEBITO) {
            lancamentoNewDTO.setValorTotal(lancamentoNewDTO.getValorTotal().negate());
        }
        BigDecimal vlrParcelas = lancamentoNewDTO.getValorTotal().divide(new BigDecimal(lancamentoNewDTO.getQtdeParcelas()), MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_EVEN);
        for (int parcelaIndex = 1; parcelaIndex <= lancamentoNewDTO.getQtdeParcelas(); parcelaIndex++) {
            int parcela = parcelaIndex;
            LocalDate dataLancamento;
            if (parcela == 1) {
                dataLancamento = lancamentoNewDTO.getDataReferencia();
                Lancamento lancamento = lancamentoMapper.newLancamentoCreate(lancamentoNewDTO, parcela, usuario, vlrParcelas, dataLancamento, natureza);
                lancamentoRepository.persist(lancamento);
            } else {
                dataLancamento = lancamentoNewDTO.getDataReferencia().plusMonths(parcela - 1);
                Lancamento lancamento = lancamentoMapper.newLancamentoCreate(lancamentoNewDTO, parcela, usuario, vlrParcelas, dataLancamento, natureza);
                lancamentoRepository.persist(lancamento);
            }
        }
    }

    @Override
    @Transactional
    public LancamentoDataDTO listarLancamentosByUsuarioDate(String username, String dataInicioStr, String dataFimStr) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataInicio = LocalDate.parse(dataInicioStr, formatter);
        LocalDate dataFim = LocalDate.parse(dataFimStr, formatter);
        List<Lancamento> lancamentosDate = lancamentoRepository.listarLancamentosByUsuarioDate(usuario, dataInicio, dataFim);

        return lancamentoMapper.listLancamentoToLancamentoDataDTO(lancamentosDate, dataInicioStr, dataFimStr);
    }

    @Override
    @Transactional
    public LancamentoDataDTO listarLancamentosByUsuarioDateFilter(LancamentoFilterDTO dtoFilter) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dtoFilter.getUsername());
        List<Lancamento> lancamentosDateFilter = lancamentoRepository.listarLancamentosByUsuarioDateFilter(usuario, dtoFilter);

        return lancamentoMapper.listLancamentoToLancamentoDataDTO(lancamentosDateFilter, dtoFilter.getDataInicio(), dtoFilter.getDataFim());
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
    public List<Origem> listarOrigem() {
        return List.of(Origem.values());
    }

    @Override
    @Transactional
    public DashboardDTO getLancamentosDashboard(String username) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);

        List<LancamentoReflectionDTO> lancamentos = lancamentoRepository.getLancamentosDashboard(usuario);
        long sumEntradas = 0;
        long sumSaidas = 0;
        Integer ano = LocalDate.now().getYear();
        for (LancamentoReflectionDTO refle : lancamentos) {
            sumEntradas = sumEntradas + refle.getEntradas();
            sumSaidas = sumSaidas + refle.getSaidas();
        }

        return lancamentoMapper.listLancamentoReflectionToDashboardDTO(lancamentos, sumEntradas, sumSaidas, ano);

    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        this.deletarPeloId(id);
    }

    @Override
    @Transactional
    public void deleteByIdList(List<String> idsDeletar) {
        for (String idDeletar : idsDeletar) {
            Long idDeletarLong = Long.parseLong(idDeletar);
            this.deletarPeloId(idDeletarLong);
        }
    }

    private void deletarPeloId(Long idLancamentoDeletar) {
        Lancamento lancamento = lancamentoRepository.findByIdOrThrow(idLancamentoDeletar);
        lancamentoRepository.deleteById(lancamento.getId());
    }

    @Override
    @Transactional
    public LancamentoUpdateDTO update(LancamentoUpdateDTO dto) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dto.getUsername());
        Natureza natureza = naturezaService.getNaturezaByUsuarioAndDescricaoOrThrow(usuario, dto.getNatureza());
        Lancamento lancamento = lancamentoRepository.findByIdAndUsuarioOrThrow(usuario, dto.getId());

        if (dto.getTipo() == TipoLancamento.DEBITO) {
            lancamento.setValorParcela(dto.getValorParcela().negate());
        }

        lancamento.setTipo(dto.getTipo());
        lancamento.setDescricao(dto.getDescricao());
        lancamento.setDataLancamento(dto.getDataLancamento());
        lancamento.setNatureza(natureza);
        lancamento.setDataAlteracao(LocalDate.now());
        lancamento.setSituacao(dto.getSituacao());
        lancamento.setOrigem(dto.getOrigem());

        lancamentoRepository.persist(lancamento);

        return dto;
    }

    @Override
    @Transactional
    public List<Lancamento> lancamentosUsuarioPorNatureza(String username, Long idNatureza) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        return lancamentoRepository.listarLancamentosUsuarioByNatureza(usuario, idNatureza);
    }

    @Override
    @Transactional
    public void uploadAnexo(AnexoUploaDTO anexoUploaDTO, Long idLancamento) {

        Lancamento lancamento = lancamentoRepository.findByIdOrThrow(idLancamento);

        byte[] anexoByte;
        try {
            anexoByte = FilesUtil.inputStreamToByteArray(anexoUploaDTO.getInputStream());
        } catch (IOException e) {
            throw new GeralException("Erro ao converter Stream para ByteArray");
        }

        if (Objects.nonNull(lancamento.getAnexo())) {
            AnexoDTO dtoUpload = new AnexoDTO();
            dtoUpload.setId(lancamento.getAnexo().getId());
            dtoUpload.setNome(anexoUploaDTO.getNome());
            dtoUpload.setType(anexoUploaDTO.getType());
            dtoUpload.setAnexoByte(anexoByte);

            lancamento.setAnexo(anexoService.atualizar(dtoUpload));
        } else {
            Anexo novoAnexo = new Anexo();
            novoAnexo.setNome(anexoUploaDTO.getNome());
            novoAnexo.setType(anexoUploaDTO.getType());
            novoAnexo.setAnexoByte(anexoByte);

            lancamento.setAnexo(anexoService.save(novoAnexo));
        }

    }

    @Override
    @Transactional
    public Lancamento findByIdOrThrow(Long idLancamento) {
        return lancamentoRepository.findByIdOrThrow(idLancamento);
    }
}
