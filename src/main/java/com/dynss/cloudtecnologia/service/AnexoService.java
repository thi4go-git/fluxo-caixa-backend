package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Anexo;
import com.dynss.cloudtecnologia.rest.dto.AnexoDTO;


public interface AnexoService {
    Anexo save(Anexo anexo);

    Anexo atualizar(AnexoDTO anexoDto);
}
