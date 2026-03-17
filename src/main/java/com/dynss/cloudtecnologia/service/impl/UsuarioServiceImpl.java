package com.dynss.cloudtecnologia.service.impl;


import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.repository.UsuarioRepository;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.rest.mapper.UsuarioMapper;
import com.dynss.cloudtecnologia.service.UsuarioService;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import java.util.List;



@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    private UsuarioRepository repository;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Inject
    private JsonWebToken jwt;


    @Override
    @Transactional
    public Usuario save(UsuarioDTO dto) {
        Usuario user = usuarioMapper.usuarioDTOtoUsuarioNew(dto);
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

    @Override
    public Usuario findByTokenJwt() {
        final String username = jwt.getClaim("preferred_username");
        return findByUsernameOrThrow(username);
    }
}
