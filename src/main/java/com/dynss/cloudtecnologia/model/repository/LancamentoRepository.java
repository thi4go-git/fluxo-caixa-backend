package com.dynss.cloudtecnologia.model.repository;


import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.LancamentoFilterDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoReflectionDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class LancamentoRepository implements PanacheRepository<Lancamento> {

    public Lancamento findByIdAndUsuarioOrThrow(Usuario usuario, Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);
        params.put("id", id);

        return find("usuario =:usuario AND id =: id", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Lancamento", "id", "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }


    public Lancamento findByIdOrThrow(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return find("id =: id", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Lancamento", "id", "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

    public List<LancamentoReflectionDTO> getLancamentosDashboard(Usuario usuario) {
        Integer ano = LocalDate.now().getYear();
        String query = "SELECT " +
                "to_char(data_lancamento,'MM/YYYY') AS mes," +
                "to_char(data_lancamento,'MM') AS mes_num," +
                "SUM(CASE WHEN tipo = 0 THEN valor_parcela ELSE 0 END) AS saldo_entradas, " +
                "SUM(CASE WHEN tipo = 1 THEN valor_parcela ELSE 0 END) AS saldo_saidas " +
                "from Lancamento " +
                "WHERE to_char(data_lancamento,'YYYY') = '" + ano + "'" +
                " AND usuario =:usuario  " +
                "GROUP BY mes,mes_num order by mes_num";

        PanacheQuery<LancamentoReflectionDTO> panache = find
                (query, Parameters.with("usuario", usuario)).project(LancamentoReflectionDTO.class);

        return panache.list();
    }

    public List<Lancamento> listarLancamentosByUsuarioDate
            (Usuario usuario, String data_inicio, String data_fim) {
        return find(" id_usuario = ?1 AND data_lancamento between '" + data_inicio + "' and '" + data_fim + "' " +
                " order by data_lancamento,id asc ", usuario.getId()).list();
    }

    public List<Lancamento> listarLancamentosUsuarioByNatureza
            (Usuario usuario, Long idNatureza) {
        return find(" id_usuario = ?1 AND id_natureza = ?2 " +
                " order by data_lancamento,id asc ", usuario.getId(), idNatureza).list();
    }

    public List<Lancamento> listarLancamentosByUsuarioDateFilter
            (Usuario usuario, LancamentoFilterDTO lancamento) {

        lancamento.setDescricao(lancamento.getDescricao().toUpperCase());

        Map<String, Object> params = new HashMap<>();
        String query = " usuario =:usuario AND  data_lancamento between '" + lancamento.getData_inicio()
                + "' AND '" + lancamento.getData_fim() + "'  ";
        params.put("usuario", usuario);

        if (lancamento.getId() != null) {
            query += " AND id = :id ";
            params.put("id", lancamento.getId());
        }
        if (lancamento.getTipo() != null) {
            if (!lancamento.getTipo().equals("TUDO")) {
                query += " AND tipo = :tipo ";
                params.put("tipo", lancamento.getTipo());
            }
        }
        if (lancamento.getDescricao() != null) {
            query += " AND UPPER(descricao) like UPPER(concat('%', :descricao, '%'))";
            params.put("descricao", lancamento.getDescricao());
        }
        if (lancamento.getValor_parcela() != null) {
            query += " AND valor_parcela = :valor_parcela ";
            params.put("valor_parcela", lancamento.getValor_parcela());
        }
        if (lancamento.getQtde_parcelas() != null) {
            query += " AND qtde_parcelas = :qtde_parcelas ";
            params.put("qtde_parcelas", lancamento.getQtde_parcelas());
        }
        if (lancamento.getNr_parcela() != null) {
            query += " AND nr_parcela = :nr_parcela  ";
            params.put("nr_parcela", lancamento.getNr_parcela());
        }
        if (lancamento.getId_natureza() != null) {
            query += " AND id_natureza = :id_natureza ";
            params.put("id_natureza", lancamento.getId_natureza());
        }
        if (lancamento.getSituacao() != null) {
            query += " AND situacao = :situacao ";
            params.put("situacao", lancamento.getSituacao());
        }

        query += " ORDER BY data_lancamento asc ";


        return find(query, params).list();

    }

}
