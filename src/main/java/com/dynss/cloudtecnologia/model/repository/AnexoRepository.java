package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.model.entity.Anexo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnexoRepository implements PanacheRepository<Anexo> {
    public Anexo findByIdOrThrow(Long id) {
        return find("id =:id",
                Parameters.with("id", id)).
                firstResultOptional()
                .orElseThrow(
                        () -> new GeralException("Anexo com id " + id + " Não localizado! "));
    }
}
