package com.dynss.cloudtecnologia.exception;

public class UsuarioNaoEncontradoException extends RuntimeException {
    public UsuarioNaoEncontradoException() {
        super("Exception: Usuário não localizado!");
    }
}
