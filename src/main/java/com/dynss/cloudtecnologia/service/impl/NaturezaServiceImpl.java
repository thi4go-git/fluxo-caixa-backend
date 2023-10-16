package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.exception.JaExisteNaturezaCadastradaParaUsername;
import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.model.repository.NaturezaRepository;
import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;
import com.dynss.cloudtecnologia.rest.mapper.NaturezaMapper;
import com.dynss.cloudtecnologia.service.NaturezaService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class NaturezaServiceImpl implements NaturezaService {

    @Inject
    private NaturezaRepository naturezaRepository;

    @Inject
    private UsuarioServiceImpl usuarioService;

    @Inject
    private LancamentoServiceImpl lancamentoService;

    @Inject
    private NaturezaMapper naturezaMapper;


    @Override
    @Transactional
    public Natureza save(NaturezaDTO dto) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(dto.getUsername());
        if (naturezaRepository.findByUsuarioAndDescricao(usuario, dto.getDescricao()).getId() == null) {
            Natureza nova = naturezaMapper.naturezaDTOtoNaturezaNew(dto, usuario);
            naturezaRepository.persist(nova);

            return nova;
        }
        throw new JaExisteNaturezaCadastradaParaUsername();
    }

    @Override
    public Natureza findById(Long id) {
        return naturezaRepository.findById(id);
    }

    @Override
    public Natureza findByIdOrThrow(Long id) {
        return naturezaRepository.findByIdOrThrow(id);
    }

    @Override
    public Natureza getNaturezaByUsuarioAndIDOrThrow(Usuario usuario, Long id) {
        return naturezaRepository.findByUsuarioAndIDOrThrow(usuario, id);
    }

    @Override
    public List<Natureza> getNaturezasByUsername(String username) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        return naturezaRepository.getNaturezasByUsuario(usuario);
    }

    @Override
    @Transactional
    public void deletarNatureza(String username, String descricaoNatureza) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        Natureza naturezaDeletar = naturezaRepository.findByUsuarioAndDescricaoThrow(usuario, descricaoNatureza);

        List<Lancamento> lancamentosPorNatureza = lancamentoService.lancamentosUsuarioPorNatureza(username, naturezaDeletar.getId());
        if (!lancamentosPorNatureza.isEmpty()) {
            throw new GeralException("Não é possível deletar: Já existem Lançamentos efetuados para essa natureza!");
        }
        naturezaRepository.delete(naturezaDeletar);
    }


}
