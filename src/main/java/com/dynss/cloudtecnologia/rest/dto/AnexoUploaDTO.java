package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;

import org.jboss.resteasy.reactive.RestForm;
import java.io.InputStream;

@Data
public class AnexoUploaDTO {
    @RestForm("anexo") // Este nome deve corresponder ao nome do campo no FormData
    public InputStream inputStream; // O arquivo será recebido como um InputStream

    @RestForm("nome") // Este nome deve corresponder ao nome do campo no FormData
    public String nome;

    @RestForm("type") // Este type deve corresponder ao nome do campo type no FormData
    private String type;
}
