package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.exception.EntidadeNaoEncontradaException;
import com.dynss.cloudtecnologia.model.entity.Anexo;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class AnexoRepository implements PanacheRepository<Anexo> {

    private static final String COLUMN_ID = "id";

    public Anexo findByIdOrThrow(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put(COLUMN_ID, id);

        return find("id =: id", params)
                .firstResultOptional()
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException("Entity não localizada: Anexo", COLUMN_ID, "" + id,
                                HttpResponseStatus.NOT_FOUND.code()));
    }
}
