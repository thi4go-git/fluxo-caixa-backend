package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.config.ObjectMapperConfig;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.rest.dto.UsuarioResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestHTTPEndpoint(UsuarioController.class)
class UsuarioControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(UsuarioControllerTest.class);
    private static final String USERNAME_EXISTENTE = "username";


    @Test
    @DisplayName("Deve Salvar")
    @Order(1)
    void deveSalvar() {

        UsuarioDTO dto = new UsuarioDTO();
        dto.setUsername(USERNAME_EXISTENTE);

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dto)
                .when()
                .post()
                .then()
                .extract()
                .response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);

        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_CREATED, resposta.statusCode());
    }

    @Test
    @DisplayName("Deve Listar Todos")
    @Order(2)
    void deveListarTodos() throws JsonProcessingException {

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get()
                .then()
                .extract().response();

        String responseBodyJson = resposta.getBody().asString();
        LOG.info(responseBodyJson);

        assertNotNull(responseBodyJson);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());

        //Converte o JSON de resposta para Objeto
        List<UsuarioResponseDTO> usuarioResponseList = ObjectMapperConfig.getObjectMapper().readValue(responseBodyJson, new TypeReference<>() {
        });

        LOG.info(usuarioResponseList.toString());
        assertTrue(!usuarioResponseList.isEmpty());
    }

    @Test
    @DisplayName("Deve Listar por ID")
    @Order(3)
    void deveListarPorId() {

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .pathParam("id", "1").get("/{id}")
                .then()
                .extract()
                .response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);

        assertNotNull(responseBody);
        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
    }

}