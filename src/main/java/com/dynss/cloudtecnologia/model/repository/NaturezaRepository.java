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

    public Natureza getNaturezaByUsuario(final Usuario usuario) {
        return find("usuario =:usuario",
                Parameters.with("usuario", usuario)).firstResultOptional()
                .orElse(new Natureza());
    }


    public Natureza findByUsuarioAndDescricao(final Usuario usuario, final String descricaoNatureza) {
        return find("usuario =:usuario AND descricao = '" + descricaoNatureza + "' ",
                Parameters.with("usuario", usuario)).firstResultOptional()
                .orElse(new Natureza());
    }

    public Natureza findByUsuarioAndID(final Usuario usuario, final Long id) {
        return find("usuario =:usuario AND id = '" + id + "' ",
                Parameters.with("usuario", usuario)).firstResultOptional()
                .orElse(new Natureza());
    }

    public Natureza findByUsuarioAndIDOrThrow(final Usuario usuario, final Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("usuario", usuario);
        params.put("id", id);
        return find("usuario =:usuario AND id =:id ", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Natureza", "id", "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }


    public List<Natureza> getNaturezasByUsuario(final Usuario usuario) {
        return find("usuario =:usuario ",
                Parameters.with("usuario", usuario)).list();
    }

}
