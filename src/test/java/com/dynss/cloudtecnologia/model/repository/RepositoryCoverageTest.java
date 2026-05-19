package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Anexo;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.enums.Origem;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.AnexoDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoFilterDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoReflectionDTO;
import com.dynss.cloudtecnologia.service.impl.AnexoServiceImpl;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

@QuarkusTest
class RepositoryCoverageTest {

    @Inject
    UsuarioRepository usuarioRepository;

    @Inject
    NaturezaRepository naturezaRepository;

    @Inject
    LancamentoRepository lancamentoRepository;

    @Inject
    AnexoRepository anexoRepository;

    @Inject
    AnexoServiceImpl anexoService;

    @Test
    @TestTransaction
    @DisplayName("Deve cobrir consultas e erros dos repositorios de usuario, natureza e anexo")
    void deveCobrirRepositoriosBasicos() {
        Usuario usuario = criarUsuario();
        Natureza natureza = criarNatureza(usuario, "Natureza Repo");

        assertSame(usuario, usuarioRepository.findByUsername(usuario.getUsername()));
        assertNull(usuarioRepository.findByUsername("ausente-" + UUID.randomUUID()).getId());
        assertSame(usuario, usuarioRepository.findByUsernameOrThrow(usuario.getUsername()));
        assertSame(usuario, usuarioRepository.findByIdOrThrow(usuario.getId()));
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> usuarioRepository.findByUsernameOrThrow("ausente-" + UUID.randomUUID()));
        assertThrows(EntidadeNaoEncontradaException.class, () -> usuarioRepository.findByIdOrThrow(-1L));

        assertSame(natureza, naturezaRepository.findByUsuarioAndDescricao(usuario, natureza.getDescricao()));
        assertNull(naturezaRepository.findByUsuarioAndDescricao(usuario, "Nao existe").getId());
        assertSame(natureza, naturezaRepository.findByUsuarioAndDescricaoThrow(usuario, natureza.getDescricao()));
        assertSame(natureza, naturezaRepository.findByUsuarioAndIDOrThrow(usuario, natureza.getId()));
        assertSame(natureza, naturezaRepository.findByIdOrThrow(natureza.getId()));
        assertEquals(1, naturezaRepository.getNaturezasByUsuario(usuario).size());
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> naturezaRepository.findByUsuarioAndDescricaoThrow(usuario, "Nao existe"));
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> naturezaRepository.findByUsuarioAndIDOrThrow(usuario, -1L));
        assertThrows(EntidadeNaoEncontradaException.class, () -> naturezaRepository.findByIdOrThrow(-1L));

        Anexo anexo = new Anexo();
        anexo.setNome("original.txt");
        anexo.setType("text/plain");
        anexo.setAnexoByte("original".getBytes());
        anexoService.save(anexo);

        AnexoDTO dto = new AnexoDTO();
        dto.setId(anexo.getId());
        dto.setNome("atualizado.txt");
        dto.setType("application/octet-stream");
        dto.setAnexoByte("atualizado".getBytes());

        Anexo atualizado = anexoService.atualizar(dto);

        assertEquals("atualizado.txt", atualizado.getNome());
        assertSame(atualizado, anexoRepository.findByIdOrThrow(anexo.getId()));
        assertThrows(EntidadeNaoEncontradaException.class, () -> anexoRepository.findByIdOrThrow(-1L));
    }

    @Test
    @TestTransaction
    @DisplayName("Deve cobrir filtros, dashboard e erros do repositorio de lancamentos")
    void deveCobrirRepositorioLancamento() {
        Usuario usuario = criarUsuario();
        Natureza natureza = criarNatureza(usuario, "Natureza Lancamento");
        LocalDate hoje = LocalDate.now();

        Lancamento credito = criarLancamento(usuario, natureza, TipoLancamento.CREDITO, "Compra Especial",
                hoje, new BigDecimal("100.00"), 2, 1, Situacao.EM_ABERTO, Origem.PROPRIO);
        Lancamento debito = criarLancamento(usuario, natureza, TipoLancamento.DEBITO, "Saida Especial",
                hoje.plusMonths(1), new BigDecimal("-40.00"), 2, 2, Situacao.PAGO, Origem.TERCEIROS);

        assertSame(credito, lancamentoRepository.findByIdAndUsuarioOrThrow(usuario, credito.getId()));
        assertSame(debito, lancamentoRepository.findByIdOrThrow(debito.getId()));
        assertThrows(EntidadeNaoEncontradaException.class,
                () -> lancamentoRepository.findByIdAndUsuarioOrThrow(usuario, -1L));
        assertThrows(EntidadeNaoEncontradaException.class, () -> lancamentoRepository.findByIdOrThrow(-1L));

        List<Lancamento> porPeriodo = lancamentoRepository.listarLancamentosByUsuarioDate(
                usuario, hoje.minusDays(1), hoje.plusMonths(2));
        assertEquals(2, porPeriodo.size());

        List<Lancamento> porNatureza = lancamentoRepository.listarLancamentosUsuarioByNatureza(usuario, natureza.getId());
        assertEquals(2, porNatureza.size());

        LancamentoFilterDTO filtroCompleto = new LancamentoFilterDTO();
        filtroCompleto.setDataInicio(hoje.minusDays(1).toString());
        filtroCompleto.setDataFim(hoje.plusDays(1).toString());
        filtroCompleto.setDescricao("compra");
        filtroCompleto.setId(credito.getId());
        filtroCompleto.setTipo(TipoLancamento.CREDITO);
        filtroCompleto.setValorParcela(new BigDecimal("100.00"));
        filtroCompleto.setQtdeParcelas(2);
        filtroCompleto.setNrParcela(1);
        filtroCompleto.setIdNatureza(natureza.getId().intValue());
        filtroCompleto.setSituacao(Situacao.EM_ABERTO);
        filtroCompleto.setOrigem(Origem.PROPRIO);

        List<Lancamento> filtrados = lancamentoRepository.listarLancamentosByUsuarioDateFilter(usuario, filtroCompleto);
        assertEquals(1, filtrados.size());
        assertSame(credito, filtrados.getFirst());

        LancamentoFilterDTO filtroMinimo = new LancamentoFilterDTO();
        filtroMinimo.setDataInicio(hoje.minusDays(1).toString());
        filtroMinimo.setDataFim(hoje.plusMonths(2).toString());
        filtroMinimo.setDescricao("");

        assertEquals(2, lancamentoRepository.listarLancamentosByUsuarioDateFilter(usuario, filtroMinimo).size());

        List<LancamentoReflectionDTO> dashboard = lancamentoRepository.getLancamentosDashboard(usuario);
        assertFalse(dashboard.isEmpty());
        assertNotNull(dashboard.getFirst().getMes());
    }

    private Usuario criarUsuario() {
        Usuario usuario = new Usuario();
        usuario.setUsername("repo-" + UUID.randomUUID().toString().substring(0, 8));
        usuarioRepository.persist(usuario);
        return usuario;
    }

    private Natureza criarNatureza(Usuario usuario, String descricao) {
        Natureza natureza = new Natureza();
        natureza.setUsuario(usuario);
        natureza.setDescricao(descricao + " " + UUID.randomUUID().toString().substring(0, 8));
        naturezaRepository.persist(natureza);
        return natureza;
    }

    private Lancamento criarLancamento(Usuario usuario, Natureza natureza, TipoLancamento tipo, String descricao,
                                       LocalDate dataLancamento, BigDecimal valorParcela, Integer qtdeParcelas,
                                       Integer nrParcela, Situacao situacao, Origem origem) {
        Lancamento lancamento = new Lancamento();
        lancamento.setUsuario(usuario);
        lancamento.setNatureza(natureza);
        lancamento.setTipo(tipo);
        lancamento.setDescricao(descricao);
        lancamento.setDataLancamento(dataLancamento);
        lancamento.setValorParcela(valorParcela);
        lancamento.setQtdeParcelas(qtdeParcelas);
        lancamento.setNrParcela(nrParcela);
        lancamento.setSituacao(situacao);
        lancamento.setOrigem(origem);
        lancamento.setDataCriacao(LocalDate.now());
        lancamentoRepository.persist(lancamento);
        return lancamento;
    }
}
