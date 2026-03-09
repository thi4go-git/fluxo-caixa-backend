package com.dynss.cloudtecnologia.service;


import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthResponseDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyTransactionsDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyWebhookDTO;



public interface PluggyService {

    PluggyAuthResponseDTO obterApikey ();

    void atualizarTransacoesItem(final String id);

    PluggyTransactionsDTO getTransacoesConta(final String idConta, final String from, final String to);

    void receberWebhook(final PluggyWebhookDTO payload);
}
