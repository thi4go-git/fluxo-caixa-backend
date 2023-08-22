package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.exception.JaExisteNaturezaCadastradaParaUsername;
import com.dynss.cloudtecnologia.exception.UsuarioNaoEncontradoException;
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
    public NaturezaDTO save(NaturezaDTO dto) {
        Usuario usuario = usuarioService.findByUsername(dto.getUsername());
        if (usuario.getId() != null) {
            if (naturezaRepository.findByUsuarioAndDescricao(usuario, dto.getDescricao()).getId() == null) {
                Natureza nova = new Natureza(dto, usuario);
                naturezaRepository.persist(nova);

                return naturezaMapper.toDto(nova);
            }
            throw new JaExisteNaturezaCadastradaParaUsername();
        }
        throw new UsuarioNaoEncontradoException();
    }

    @Override
    public Natureza findById(Long id) {
        return naturezaRepository.findById(id);
    }

    @Override
    public Natureza getNaturezaByUsuarioAndIDOrThrow(Usuario usuario, Long id) {
        return naturezaRepository.findByUsuarioAndIDOrThrow(usuario, id);
    }

    @Override
    public List<NaturezaDTO> getNaturezasByUsername(String username) {
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        List<Natureza> naturezas = naturezaRepository.getNaturezasByUsuario(usuario);
        return naturezaMapper.listNaturezaTolistDTO(naturezas);
    }

    @Override
    @Transactional
    public void deletarNatureza(String username, String descricaoNatureza) {
        System.out.println("");
        Usuario usuario = usuarioService.findByUsernameOrThrow(username);
        Natureza naturezaDeletar =  naturezaRepository.findByUsuarioAndDescricaoThrow(usuario,descricaoNatureza);

        List<Lancamento> lancamentosPorNatureza = lancamentoService.lancamentosUsuarioPorNatureza(username,naturezaDeletar.getId());
        if(!lancamentosPorNatureza.isEmpty()){
            throw new GeralException("Não é possível deletar: Já existem Lançamentos efetuados para essa natureza!");
        }

        naturezaRepository.delete(naturezaDeletar);

    }


}
