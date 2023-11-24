package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.model.entity.Anexo;
import com.dynss.cloudtecnologia.model.repository.AnexoRepository;
import com.dynss.cloudtecnologia.rest.dto.AnexoDTO;
import com.dynss.cloudtecnologia.rest.dto.AnexoUploaDTO;
import com.dynss.cloudtecnologia.service.AnexoService;
import com.dynss.cloudtecnologia.utils.FilesUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;

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

    @Override
    @Transactional
    public Anexo atualizar(AnexoDTO dto) {
        Anexo anexoUpdate = anexoRepository.findByIdOrThrow(dto.getId());
        anexoUpdate.setNome(dto.getNome());
        anexoUpdate.setType(dto.getType());
        anexoUpdate.setAnexoByte(dto.getAnexoByte());
   
        anexoRepository.persist(anexoUpdate);
        return anexoUpdate;
    }
}
