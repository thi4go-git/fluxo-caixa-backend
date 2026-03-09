package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.client.PluggyClient;
import com.dynss.cloudtecnologia.model.enums.TipoEventoWebhook;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthRequestDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthResponseDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyTransactionsDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyWebhookDTO;
import com.dynss.cloudtecnologia.service.PluggyService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;



@ApplicationScoped
public class PluggyServiceImpl implements PluggyService {

    private static final Logger LOG = Logger.getLogger(PluggyServiceImpl.class);

    @ConfigProperty(name = "pluggy.webhook.clientId")
    String clientId;

    @ConfigProperty(name = "pluggy.webhook.clientSecret")
    String clientSecret;

    @ConfigProperty(name = "pluggy.webhook.idConta")
    String idConta;

    private static final String FROM = "2026-03-01";
    private static final String TO = "2026-03-10";

    @Inject
    @RestClient
    private PluggyClient pluggyClient;


    @Override
    public PluggyAuthResponseDTO obterApikey() {
        PluggyAuthRequestDTO body = PluggyAuthRequestDTO
                .builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        return pluggyClient.auth(body);
    }

    @Override
    public void atualizarTransacoesItem(final String id) {
         LOG.info("Iniciando atualização de transações Pluggy");
         PluggyAuthResponseDTO response = obterApikey();
         pluggyClient.patchItem(id, response.getApiKey());
    }

    @Override
    public PluggyTransactionsDTO getTransacoesConta(
            final String idConta, final String from, final String to
    ) {
        PluggyAuthResponseDTO response = obterApikey();
        return pluggyClient.getTransactions(
                response.getApiKey(), idConta, from, to
        );
    }

    @Override
    public void receberWebhook(final PluggyWebhookDTO webhook) {
        LOG.infof(
                "Webhook Pluggy RECEBIDO | event=%s | itemId=%s | connectorId=%s",
                webhook.getEvent(),
                webhook.getItemId(),
                webhook.getConnectorId()
        );
        TipoEventoWebhook eventoRecebido = TipoEventoWebhook.fromDescricao(webhook.getEvent());
        switch (eventoRecebido) {
            case ITEM_UPDATED:
                PluggyTransactionsDTO transactions = getTransacoesConta(idConta, FROM, TO);
                break;
            case TRANSACTIONS_CREATED:
                LOG.info("Novas transações disponíveis");
                break;
            case ITEM_ERROR:
                LOG.error("Erro ao atualizar item");
                break;
            default:
                LOG.info("Evento não tratado: ");
        }
    }


}
