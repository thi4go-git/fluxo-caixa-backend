package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.config.ModelMapperConfig;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.rest.dto.UsuarioResponseDTO;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class UsuarioMapper {

    public UsuarioDTO usuarioToUsuarioDTO(Usuario usuario) {
        return ModelMapperConfig.getModelMapper().map(usuario, UsuarioDTO.class);
    }

    public UsuarioResponseDTO usuarioToUsuarioResponseDTO(Usuario usuario) {
        return ModelMapperConfig.getModelMapper().map(usuario, UsuarioResponseDTO.class);
    }

    public Usuario usuarioDTOtoUsuarioNew(UsuarioDTO usuarioDTO) {
        return ModelMapperConfig.getModelMapper().map(usuarioDTO, Usuario.class);
    }

    public List<UsuarioDTO> listUsuarioToListUsuarioDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(this::usuarioToUsuarioDTO).collect(Collectors.toList());
    }

    public List<UsuarioResponseDTO> listUsuarioToListUsuarioResponseDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(this::usuarioToUsuarioResponseDTO).collect(Collectors.toList());
    }

}
