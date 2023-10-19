package com.dynss.cloudtecnologia.model.repository;


import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.LancamentoFilterDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoReflectionDTO;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        String ano = "" + LocalDate.now().getYear();
        String query = "SELECT " +
                "to_char(data_lancamento,'MM/YYYY') AS mes," +
                "to_char(data_lancamento,'MM') AS mesnumero," +
                "SUM(CASE WHEN tipo = 0 THEN valor_parcela ELSE 0 END) AS entradas, " +
                "SUM(CASE WHEN tipo = 1 THEN valor_parcela ELSE 0 END) AS saidas " +
                "from Lancamento " +
                "WHERE to_char(data_lancamento,'YYYY') = '" + ano + "'" +
                " AND usuario =:usuario  " +
                "GROUP BY mes,mesnumero order by mesnumero";

        return find
                (query, Parameters.with(COLUMN_USUARIO, usuario))
                .project(LancamentoReflectionDTO.class).list();
    }

    public List<Lancamento> listarLancamentosByUsuarioDate
            (Usuario usuario, LocalDate dataInicio, LocalDate dataFim) {
        return find(" id_usuario = ?1 AND data_lancamento between ?2 and ?3 " +
                " order by data_lancamento,id asc ", usuario.getId(), dataInicio, dataFim).list();
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
        String query = " usuario =:usuario AND  data_lancamento between '" + lancamento.getDataInicio()
                + "' AND '" + lancamento.getDataFim() + "'  ";
        params.put(COLUMN_USUARIO, usuario);

        if (lancamento.getId() != null) {
            query += " AND id = :id ";
            params.put(COLUMN_ID, lancamento.getId());
        }
        if (lancamento.getTipo() != null &&
                !lancamento.getTipo().equals("TUDO")) {
            query += " AND tipo = :tipo ";
            params.put("tipo", lancamento.getTipo());
        }
        if (lancamento.getDescricao() != null) {
            query += " AND UPPER(descricao) like UPPER(concat('%', :descricao, '%'))";
            params.put("descricao", lancamento.getDescricao());
        }
        if (lancamento.getValorParcela() != null) {
            query += " AND valor_parcela = :valor_parcela ";
            params.put("valor_parcela", lancamento.getValorParcela());
        }
        if (lancamento.getQtdeParcelas() != null) {
            query += " AND qtde_parcelas = :qtde_parcelas ";
            params.put("qtde_parcelas", lancamento.getQtdeParcelas());
        }
        if (lancamento.getNrParcela() != null) {
            query += " AND nr_parcela = :nr_parcela  ";
            params.put("nr_parcela", lancamento.getNrParcela());
        }
        if (lancamento.getIdNatureza() != null) {
            query += " AND id_natureza = :id_natureza ";
            params.put("id_natureza", lancamento.getIdNatureza());
        }
        if (lancamento.getSituacao() != null) {
            query += " AND situacao = :situacao ";
            params.put("situacao", lancamento.getSituacao());
        }

        query += " ORDER BY data_lancamento asc ";


        return find(query, params).list();

    }

}
