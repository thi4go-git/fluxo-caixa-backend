package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.NaturezaNewDTO;
import java.util.List;


public interface NaturezaService {
    Natureza save(NaturezaNewDTO dto);

    Natureza findByIdOrThrow(Long id);

    Natureza getNaturezaByUsuarioAndDescricaoOrThrow(final Usuario usuario, final String descricaoNatureza);

    List<Natureza>  getNaturezasByUsername(String username);

    void deletarNatureza(final String username, final String descricaoNatureza);
}
