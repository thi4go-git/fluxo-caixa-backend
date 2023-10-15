package com.dynss.cloudtecnologia.service.impl;


import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.repository.UsuarioRepository;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.service.UsuarioService;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    private UsuarioRepository repository;


    @Override
    @Transactional
    public Usuario save(UsuarioDTO dto) {
        Usuario user = new Usuario();
        user.setUsername(dto.getUsername().trim());
        repository.persist(user);

        return user;
    }

    @Override
    public Usuario findById(Long id) {
        return repository.findByIdOrThrow(id);
    }

    @Override
    public List<Usuario> findAll() {
        return repository.findAll().list();
    }


    @Override
    public Usuario findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public Usuario findByUsernameOrThrow(String username) {
        return repository.findByUsernameOrThrow(username);
    }
}
