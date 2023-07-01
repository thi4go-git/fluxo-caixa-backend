package com.dynss.cloudtecnologia.exception;

public class JaExisteNaturezaCadastradaParaUsername extends RuntimeException {
    public JaExisteNaturezaCadastradaParaUsername() {
        super("Já existe essa natureza cadastrada para esse usuário!");
    }
}
