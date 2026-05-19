package com.dynss.cloudtecnologia.anottation.impl;

import com.dynss.cloudtecnologia.anottation.UsernameUnico;
import com.dynss.cloudtecnologia.service.impl.UsuarioServiceImpl;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UsernameUnicoImpl implements ConstraintValidator<UsernameUnico, String> {


    @Inject
    private UsuarioServiceImpl usuarioService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return usuarioService.findByUsername(username).getId() == null;
    }
}
