package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;


@Data
public class AnexoDTO {
    private Long id;
    private String nome;
    private String type;
    private byte[] anexoByte;
}
