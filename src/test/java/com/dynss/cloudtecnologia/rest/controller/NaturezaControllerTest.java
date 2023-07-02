package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(NaturezaController.class)
class NaturezaControllerTest {


    private static final Logger LOG = LoggerFactory.getLogger(UsuarioControllerTest.class);


    @Test
    @DisplayName("Chamar Salvar")
    @Order(1)
    void chamarSalvar() {

        NaturezaDTO dto = new NaturezaDTO();
        dto.setDescricao("descrição");
        dto.setUsername("username");

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .extract().response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);

        assertNotNull(responseBody);
    }

    @Test
    @DisplayName("obter naturezas por username")
    @Order(2)
    void deveObter() {

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username","teste")
                .when()
                .get()
                .then()
                .extract().response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);

        assertNotNull(responseBody);
    }


}