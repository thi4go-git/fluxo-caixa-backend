package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;

import javax.ws.rs.FormParam;
import java.io.InputStream;

@Data
public class AnexoUploaDTO {
    @FormParam("anexo") // Este nome deve corresponder ao nome do campo no FormData
    public InputStream inputStream; // O arquivo será recebido como um InputStream

    @FormParam("nome") // Este nome deve corresponder ao nome do campo no FormData
    public String nome;

    @FormParam("type") // Este type deve corresponder ao nome do campo type no FormData
    private String type;
}
