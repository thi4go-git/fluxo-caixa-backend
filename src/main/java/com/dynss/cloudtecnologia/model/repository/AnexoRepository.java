package com.dynss.cloudtecnologia.model.repository;

import com.dynss.cloudtecnologia.model.entity.Anexo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AnexoRepository implements PanacheRepository<Anexo> {
}
