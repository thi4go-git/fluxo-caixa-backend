package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.config.ModelMapperConfig;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.NaturezaNewDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class NaturezaMapper {

    public NaturezaNewDTO toDto(final Natureza natureza) {
        NaturezaNewDTO dto = ModelMapperConfig.getModelMapper().map(natureza, NaturezaNewDTO.class);
        dto.setUsername(natureza.getUsuario().getUsername());

        return dto;
    }

    public List<NaturezaNewDTO> listNaturezaTolistDTO(List<Natureza> naturezas) {
        return naturezas.stream().map(this::toDto).collect(Collectors.toList());
    }

    public Natureza naturezaDTOtoNaturezaNew(NaturezaNewDTO dto, Usuario usuario) {
        Natureza nova = new Natureza();
        nova.setDescricao(dto.getDescricao());
        nova.setUsuario(usuario);

        return nova;
    }

}
