package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.client.PluggyClient;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthRequestDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthResponseDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyTransactionsDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyWebhookDTO;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.mockito.ArgumentCaptor;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class PluggyServiceImplTest {

    private static final String API_KEY = "api-key";

    @Inject
    PluggyServiceImpl service;

    @InjectMock
    @RestClient
    PluggyClient pluggyClient;

    @ConfigProperty(name = "pluggy.webhook.clientId")
    String clientId;

    @ConfigProperty(name = "pluggy.webhook.clientSecret")
    String clientSecret;

    @ConfigProperty(name = "pluggy.webhook.idConta")
    String idConta;

    private PluggyTransactionsDTO transactions;

    @BeforeEach
    void setUp() {
        transactions = PluggyTransactionsDTO.builder().build();

        PluggyAuthResponseDTO authResponse = new PluggyAuthResponseDTO();
        authResponse.setApiKey(API_KEY);
        when(pluggyClient.auth(any())).thenReturn(authResponse);
        when(pluggyClient.getTransactions(API_KEY, idConta, "2026-03-01", "2026-03-10"))
                .thenReturn(transactions);
    }

    @Test
    @DisplayName("Deve obter api key com credenciais configuradas")
    void deveObterApiKey() {
        PluggyAuthResponseDTO resposta = service.obterApikey();

        ArgumentCaptor<PluggyAuthRequestDTO> captor = ArgumentCaptor.forClass(PluggyAuthRequestDTO.class);
        verify(pluggyClient).auth(captor.capture());
        assertEquals(API_KEY, resposta.getApiKey());
        assertEquals(clientId, captor.getValue().getClientId());
        assertEquals(clientSecret, captor.getValue().getClientSecret());
    }

    @Test
    @DisplayName("Deve atualizar transacoes do item")
    void deveAtualizarTransacoesItem() {
        service.atualizarTransacoesItem("item-id");

        verify(pluggyClient).patchItem("item-id", API_KEY);
    }

    @Test
    @DisplayName("Deve buscar transacoes da conta")
    void deveBuscarTransacoesConta() {
        PluggyTransactionsDTO resposta = service.getTransacoesConta(idConta, "2026-03-01", "2026-03-10");

        assertSame(transactions, resposta);
        verify(pluggyClient).getTransactions(API_KEY, idConta, "2026-03-01", "2026-03-10");
    }

    @Test
    @DisplayName("Deve tratar webhook de item atualizado")
    void deveTratarWebhookItemAtualizado() {
        service.receberWebhook(criarWebhook("item/updated"));

        verify(pluggyClient).getTransactions(API_KEY, idConta, "2026-03-01", "2026-03-10");
    }

    @Test
    @DisplayName("Deve tratar webhook de transacoes criadas sem buscar transacoes")
    void deveTratarWebhookTransacoesCriadas() {
        service.receberWebhook(criarWebhook("transactions/created"));

        verify(pluggyClient, never()).getTransactions(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve tratar webhook de erro de item")
    void deveTratarWebhookItemErro() {
        service.receberWebhook(criarWebhook("item/error"));

        verify(pluggyClient, never()).getTransactions(any(), any(), any(), any());
    }

    @Test
    @DisplayName("Deve rejeitar evento de webhook invalido")
    void deveRejeitarEventoWebhookInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service.receberWebhook(criarWebhook("evento/invalido")));
    }

    private PluggyWebhookDTO criarWebhook(String evento) {
        return PluggyWebhookDTO.builder()
                .event(evento)
                .itemId(UUID.randomUUID())
                .connectorId(1L)
                .build();
    }

}
