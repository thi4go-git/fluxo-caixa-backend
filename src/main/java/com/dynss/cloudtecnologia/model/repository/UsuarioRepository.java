package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public Usuario findByUsername(String username) {
        return find("username =:username", Parameters.with("username", username)).firstResultOptional()
                .orElse(new Usuario());
    }

    public Usuario findByUsernameOrThrow(String username) {
        return find("username =:username",
                Parameters.with("username", username)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Usuario", "username", username,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

    public Usuario findByIdOrThrow(Long id) {
        return find("id =:id",
                Parameters.with("id", id)).firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity: Usuario", "id", "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }

}
