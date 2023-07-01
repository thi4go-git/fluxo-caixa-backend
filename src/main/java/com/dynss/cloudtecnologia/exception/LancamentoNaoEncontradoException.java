package com.dynss.cloudtecnologia.exception;

public class LancamentoNaoEncontradoException extends RuntimeException {
    public LancamentoNaoEncontradoException() {
        super("Exception: Lançamento não localizado!");
    }
}