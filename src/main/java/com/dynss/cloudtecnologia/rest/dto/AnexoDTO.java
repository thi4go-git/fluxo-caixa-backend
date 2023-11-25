package com.dynss.cloudtecnologia.rest.dto;

import lombok.Data;


@Data
public class AnexoDTO {
    Long id;
    String nome;
    String type;
    byte[] anexoByte;
}
