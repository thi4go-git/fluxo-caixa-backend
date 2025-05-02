package com.dynss.cloudtecnologia.model.enums;

import lombok.Getter;

@Getter
public enum TipoOperacaoLancamento {
    DELETAR(1),
    MARCAR_COMO_PAGO(2);

    private final int id;

    TipoOperacaoLancamento(int id) {
        this.id = id;
    }

    public static TipoOperacaoLancamento fromId(int id) {
        for (TipoOperacaoLancamento tipo : values()) {
            if (tipo.getId() == id) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("id ENUM TipoOperacaoLancamento inválido: " + id);
    }
}
