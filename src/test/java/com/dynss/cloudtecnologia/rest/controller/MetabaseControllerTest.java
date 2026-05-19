package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.rest.dto.metabase.MetabaseEmbedResponseDTO;
import com.dynss.cloudtecnologia.service.MetabaseService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;



@QuarkusTest
class MetabaseControllerTest {

    @InjectMock
    MetabaseService metabaseService;

    @Test
    @DisplayName("Deve retornar token de dashboard")
    void deveRetornarTokenDashboard() {
        when(metabaseService.gerarTokenDashboard())
                .thenReturn(new MetabaseEmbedResponseDTO("token-dashboard", "https://metabase.local"));

        var resposta = given()
                .when()
                .get("/metabase/dashboard-url-token")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertEquals("token-dashboard", resposta.jsonPath().getString("token"));
        assertEquals("https://metabase.local", resposta.jsonPath().getString("instanceUrl"));
        verify(metabaseService).gerarTokenDashboard();
    }
}
