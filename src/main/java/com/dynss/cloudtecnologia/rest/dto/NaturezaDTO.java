package com.dynss.cloudtecnologia.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NaturezaDTO {

    @NotBlank(message = "{campo.descricao.obrigatorio}")
    private String descricao;

    @NotBlank(message = "{campo.username.obrigatorio}")
    private String username;
}
