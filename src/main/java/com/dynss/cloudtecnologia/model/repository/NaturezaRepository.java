package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;

import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class NaturezaRepository implements PanacheRepository<Natureza> {

    private static final String COLUMN_USUARIO = "usuario";
    private static final String COLUMN_ID = "id";

    public Natureza findByUsuarioAndDescricao(final Usuario usuario, final String descricaoNatureza) {
        return find("usuario =:usuario AND descricao = '" + descricaoNatureza + "' ",
                Parameters.with(COLUMN_USUARIO, usuario)).firstResultOptional()
                .orElse(new Natureza());
    }

    public Natureza findByUsuarioAndDescricaoThrow(final Usuario usuario, final String descricaoNatureza) {
        return find("usuario =:usuario AND descricao = '" + descricaoNatureza + "' ",
                Parameters.with(COLUMN_USUARIO, usuario)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza", "descricao", descricaoNatureza,
                                HttpResponseStatus.NOT_FOUND.code()));
    }


    public Natureza findByUsuarioAndIDOrThrow(final Usuario usuario, final Long idNatureza) {
        Map<String, Object> params = new HashMap<>();
        params.put(COLUMN_USUARIO, usuario);
        params.put(COLUMN_ID, idNatureza);
        return find("usuario =:usuario AND id =:idNatureza ", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza", COLUMN_ID, "" + idNatureza,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

    public List<Natureza> getNaturezasByUsuario(final Usuario usuario) {
        return find("usuario =:usuario ",
                Parameters.with(COLUMN_USUARIO, usuario)).list();
    }

    public Natureza findByIdOrThrow(Long id) {
        return find("id =:id",
                Parameters.with(COLUMN_ID, id)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza não encontrada: ", COLUMN_ID, "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

}
