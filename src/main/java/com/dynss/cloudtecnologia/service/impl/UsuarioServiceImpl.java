package com.dynss.cloudtecnologia.service.impl;


import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.repository.UsuarioRepository;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.rest.mapper.UsuarioMapper;
import com.dynss.cloudtecnologia.service.UsuarioService;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class UsuarioServiceImpl implements UsuarioService {

    @Inject
    private UsuarioRepository repository;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Override
    @Transactional
    public UsuarioDTO save(UsuarioDTO dto) {
        Usuario user = new Usuario();
        user.setUsername(dto.getUsername().trim());
        repository.persist(user);
        //
        return usuarioMapper.toDto(user);
    }

    @Override
    public UsuarioDTO findById(Long id) {
        Usuario userAchado = repository.findByIdOrThrow(id);
        return usuarioMapper.toDto(userAchado);
    }

    @Override
    public List<UsuarioDTO> findAll() {
        List<Usuario> listaUsers = repository.findAll().list();
        return usuarioMapper.listToDTO(listaUsers);
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
