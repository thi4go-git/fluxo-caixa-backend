package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyWebhookDTO;
import com.dynss.cloudtecnologia.service.PluggyService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.UUID;
import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;



@QuarkusTest
class PluggyControllerTest {

    @InjectMock
    PluggyService pluggyService;

    @ConfigProperty(name = "pluggy.webhook.secret")
    String webhookSecret;

    @Test
    @DisplayName("Nao deve aceitar webhook sem secret correto")
    void naoDeveAceitarWebhookSemSecretCorreto() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(criarWebhook())
                .when()
                .post("/pluggy/webhooks")
                .then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED);

        verify(pluggyService, never()).receberWebhook(any());
    }

    @Test
    @DisplayName("Deve aceitar webhook com secret correto")
    void deveAceitarWebhookComSecretCorreto() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, webhookSecret)
                .body(criarWebhook())
                .when()
                .post("/pluggy/webhooks")
                .then()
                .statusCode(HttpStatus.SC_OK);

        verify(pluggyService).receberWebhook(any());
    }

    private PluggyWebhookDTO criarWebhook() {
        return PluggyWebhookDTO.builder()
                .event("item/updated")
                .itemId(UUID.randomUUID())
                .eventId(UUID.randomUUID())
                .connectorId(1L)
                .build();
    }
}
