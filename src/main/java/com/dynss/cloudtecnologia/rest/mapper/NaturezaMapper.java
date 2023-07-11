package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class NaturezaMapper {

    public NaturezaDTO toDto(final Natureza natureza) {
        NaturezaDTO dto = new NaturezaDTO();
        dto.setId(natureza.getId());
        dto.setDescricao(natureza.getDescricao());
        dto.setUsername(natureza.getUsuario().getUsername());
        return dto;
    }

    public List<NaturezaDTO> listNaturezaTolistDTO(List<Natureza> naturezas) {
        List<NaturezaDTO> listNaturezaDTOS = new ArrayList<>();
        for (Natureza natureza : naturezas) {
            listNaturezaDTOS.add(toDto(natureza));
        }
        return listNaturezaDTOS;
    }
}
