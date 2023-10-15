package com.dynss.cloudtecnologia.service;

import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;

import java.util.List;


public interface UsuarioService {
    Usuario save(UsuarioDTO dto);

    Usuario findById(Long id);

    List<Usuario> findAll();

    Usuario findByUsername(String username);

    Usuario findByUsernameOrThrow(String username);
}
