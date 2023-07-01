package com.dynss.cloudtecnologia.rest.dto;

import com.dynss.cloudtecnologia.anottation.UsernameUnico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UsernameUnico
    private String username;
}
