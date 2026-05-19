package com.dynss.cloudtecnologia.model.repository;


import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.LancamentoFilterDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoReflectionDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@ApplicationScoped
public class LancamentoRepository implements PanacheRepository<Lancamento> {

    private static final String COLUMN_USUARIO = "usuario";
    private static final String COLUMN_ID = "id";

    public Lancamento findByIdAndUsuarioOrThrow(Usuario usuario, Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put(COLUMN_USUARIO, usuario);
        params.put(COLUMN_ID, id);

        return find("usuario =:usuario AND id =: id", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Lancamento", COLUMN_ID, "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }


    public Lancamento findByIdOrThrow(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put(COLUMN_ID, id);

        return find("id =: id", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity não localizada: Lancamento", COLUMN_ID, "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

    public List<LancamentoReflectionDTO> getLancamentosDashboard(Usuario usuario) {
        int ano = LocalDate.now().getYear();
        LocalDate inicioAno = LocalDate.of(ano, 1, 1);
        LocalDate fimAno = LocalDate.of(ano, 12, 31);

        List<Lancamento> lancamentos = find(
                "usuario = :usuario AND dataLancamento between :inicioAno AND :fimAno order by dataLancamento asc",
                Parameters.with(COLUMN_USUARIO, usuario)
                        .and("inicioAno", inicioAno)
                        .and("fimAno", fimAno)
        ).list();

        Map<Integer, List<Lancamento>> lancamentosPorMes = lancamentos.stream()
                .collect(Collectors.groupingBy(
                        lancamento -> lancamento.getDataLancamento().getMonthValue(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        return lancamentosPorMes.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> toLancamentoReflectionDTO(ano, entry.getKey(), entry.getValue()))
                .toList();
    }

    private LancamentoReflectionDTO toLancamentoReflectionDTO(int ano, int mes, List<Lancamento> lancamentos) {
        long entradas = lancamentos.stream()
                .filter(lancamento -> lancamento.getTipo() == TipoLancamento.CREDITO)
                .map(Lancamento::getValorParcela)
                .map(BigDecimal::longValue)
                .reduce(0L, Long::sum);

        long saidas = lancamentos.stream()
                .filter(lancamento -> lancamento.getTipo() == TipoLancamento.DEBITO)
                .map(Lancamento::getValorParcela)
                .map(BigDecimal::longValue)
                .reduce(0L, Long::sum);

        String mesNumero = String.format("%02d", mes);
        return new LancamentoReflectionDTO(mesNumero + "/" + ano, mesNumero, entradas, saidas);
    }

    public List<Lancamento> listarLancamentosByUsuarioDate
            (Usuario usuario, LocalDate dataInicio, LocalDate dataFim) {
        return find(" usuario = ?1 AND dataLancamento between ?2 and ?3 " +
                " order by dataLancamento,id asc ", usuario, dataInicio, dataFim).list();
    }

    public List<Lancamento> listarLancamentosUsuarioByNatureza
            (Usuario usuario, Long idNatureza) {
        return find(" usuario = ?1 AND natureza.id = ?2 " +
                " order by dataLancamento,id asc ", usuario, idNatureza).list();
    }

    public List<Lancamento> listarLancamentosByUsuarioDateFilter
            (Usuario usuario, LancamentoFilterDTO lancamento) {

        lancamento.setDescricao(lancamento.getDescricao().toUpperCase());

        Map<String, Object> params = new HashMap<>();
        String query = " usuario = :usuario AND dataLancamento between :dataInicio AND :dataFim ";
        params.put(COLUMN_USUARIO, usuario);
        params.put("dataInicio", LocalDate.parse(lancamento.getDataInicio()));
        params.put("dataFim", LocalDate.parse(lancamento.getDataFim()));

        if (lancamento.getId() != null) {
            query += " AND id = :id ";
            params.put(COLUMN_ID, lancamento.getId());
        }
        if (lancamento.getTipo() != null) {
            query += " AND tipo = :tipo ";
            params.put("tipo", lancamento.getTipo());
        }
        if (lancamento.getDescricao() != null && !lancamento.getDescricao().isBlank()) {
            query += " AND UPPER(descricao) like UPPER(concat('%', :descricao, '%'))";
            params.put("descricao", lancamento.getDescricao());
        }
        if (lancamento.getValorParcela() != null) {
            query += " AND valorParcela = :valorParcela ";
            params.put("valorParcela", lancamento.getValorParcela());
        }
        if (lancamento.getQtdeParcelas() != null) {
            query += " AND qtdeParcelas = :qtdeParcelas ";
            params.put("qtdeParcelas", lancamento.getQtdeParcelas());
        }
        if (lancamento.getNrParcela() != null) {
            query += " AND nrParcela = :nrParcela  ";
            params.put("nrParcela", lancamento.getNrParcela());
        }
        if (lancamento.getIdNatureza() != null) {
            query += " AND natureza.id = :idNatureza ";
            params.put("idNatureza", lancamento.getIdNatureza());
        }
        if (lancamento.getSituacao() != null) {
            query += " AND situacao = :situacao ";
            params.put("situacao", lancamento.getSituacao());
        }
        if (lancamento.getOrigem() != null) {
            query += " AND origem = :origem ";
            params.put("origem", lancamento.getOrigem());
        }

        query += " ORDER BY dataLancamento asc ";


        return find(query, params).list();

    }

}
