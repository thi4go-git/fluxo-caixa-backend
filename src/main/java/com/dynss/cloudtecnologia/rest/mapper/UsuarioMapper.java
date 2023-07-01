package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class UsuarioMapper {

    public UsuarioDTO toDto(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername(usuario.getUsername());
        return dto;
    }

    public List<UsuarioDTO> listToDTO(List<Usuario> usuarios) {
        List<UsuarioDTO> listaDto = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            listaDto.add(toDto(usuario));
        }
        return listaDto;
    }

}
