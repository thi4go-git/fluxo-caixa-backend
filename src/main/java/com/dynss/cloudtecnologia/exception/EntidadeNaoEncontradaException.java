package com.dynss.cloudtecnologia.exception;

public class EntidadeNaoEncontradaException extends RuntimeException {

    public EntidadeNaoEncontradaException(
             String nomeEntidade,  String nomeCampo,  String valorCampo,
             Integer statusCode
    ) {
        super("Objeto não encontrado: " + nomeEntidade + ", nomeCampo=" + nomeCampo + ", valorCampo" +
                "=" + valorCampo + ", statusCode=" + statusCode);
    }
}
