package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.model.entity.Anexo;
import com.dynss.cloudtecnologia.model.repository.AnexoRepository;
import com.dynss.cloudtecnologia.service.AnexoService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class AnexoServiceImpl implements AnexoService {


    @Inject
    private AnexoRepository anexoRepository;

    @Override
    @Transactional
    public Anexo save(Anexo anexo) {
        anexoRepository.persist(anexo);
        return anexo;
    }
}
