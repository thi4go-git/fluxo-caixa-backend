package com.dynss.cloudtecnologia.rest.mapper;

import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.rest.dto.AnexoDownloaDTO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@Slf4j
public class AnexoMapper {

    public AnexoDownloaDTO lancamentoToAnexoDownloaDTO(Lancamento lancamento) {
        AnexoDownloaDTO anexoDownloaDTO = new AnexoDownloaDTO();
        anexoDownloaDTO.setNome(lancamento.getAnexo().getNome());
        anexoDownloaDTO.setType(lancamento.getAnexo().getType());
        anexoDownloaDTO.setAnexo(lancamento.getAnexo().getAnexoByte());

        return anexoDownloaDTO;
    }

}
