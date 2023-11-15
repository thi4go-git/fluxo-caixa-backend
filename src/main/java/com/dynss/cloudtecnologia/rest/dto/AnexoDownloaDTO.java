package com.dynss.cloudtecnologia.rest.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnexoDownloaDTO {
    String nome;
    String type;
    byte[] anexo;
}
