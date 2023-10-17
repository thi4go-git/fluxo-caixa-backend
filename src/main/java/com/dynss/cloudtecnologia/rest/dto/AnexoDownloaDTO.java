package com.dynss.cloudtecnologia.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnexoDownloaDTO {
    private String nome;
    private String type;
    private byte[] anexo;
}
