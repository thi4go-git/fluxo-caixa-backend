package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class NaturezaMapper {

    public NaturezaDTO naturezaToDTO(final Natureza natureza) {
        NaturezaDTO naturezaDTO = new NaturezaDTO();
        naturezaDTO.setDescricao(natureza.getDescricao());
        naturezaDTO.setUsername(natureza.getUsuario().getUsername());
        return naturezaDTO;
    }

    public List<NaturezaDTO> listNaturezaTolistDTO(List<Natureza> naturezas) {
        List<NaturezaDTO> listNaturezaDTOS = new ArrayList<>();
        for (Natureza natureza : naturezas) {
            listNaturezaDTOS.add(naturezaToDTO(natureza));
        }
        return listNaturezaDTOS;
    }
}
