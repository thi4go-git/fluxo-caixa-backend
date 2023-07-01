package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;

import java.util.List;


public interface UsuarioService {
    UsuarioDTO save(UsuarioDTO dto);

    UsuarioDTO findById(Long id);

    List<UsuarioDTO> findAll();

    Usuario findByUsername(String username);

    Usuario findByUsernameOrThrow(String username);
}
