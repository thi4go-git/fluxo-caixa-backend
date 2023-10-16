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


    public Natureza findByUsuarioAndDescricao(final Usuario usuario, final String descricaoNatureza) {
        return find("usuario =:usuario AND descricao = '" + descricaoNatureza + "' ",
                Parameters.with("usuario", usuario)).firstResultOptional()
                .orElse(new Natureza());
    }

    public Natureza findByUsuarioAndDescricaoThrow(final Usuario usuario, final String descricaoNatureza) {
        return find("usuario =:usuario AND descricao = '" + descricaoNatureza + "' ",
                Parameters.with("usuario", usuario)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza", "descricao", "" + descricaoNatureza,
                                HttpResponseStatus.NOT_FOUND.code()));
    }


    public Natureza findByUsuarioAndIDOrThrow(final Usuario usuario, final Long idNatureza) {
        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);
        params.put("id", idNatureza);
        return find("usuario =:usuario AND id =:idNatureza ", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza", "id", "" + idNatureza,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

    public List<Natureza> getNaturezasByUsuario(final Usuario usuario) {
        return find("usuario =:usuario ",
                Parameters.with("usuario", usuario)).list();
    }

    public Natureza findByIdOrThrow(Long id) {
        return find("id =:id",
                Parameters.with("id", id)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza não encontrada: ", "id", "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

}
