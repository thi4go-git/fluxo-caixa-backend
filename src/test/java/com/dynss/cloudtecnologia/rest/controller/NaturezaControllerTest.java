package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.rest.dto.NaturezaNewDTO;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
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
class NaturezaControllerTest {


    private static final Logger LOG = LoggerFactory.getLogger(UsuarioControllerTest.class);
    private static final String USERNAME_EXISTENTE = "username2";
    private static final String TAG_USERNAME = "username";


    @Test
    @DisplayName("Não deve criar uma Natureza com DTO vazio")
    @Order(1)
    @TestHTTPEndpoint(NaturezaController.class)
    void naoCriarNatureza() {

        NaturezaNewDTO dto = new NaturezaNewDTO();

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
        String mensagemRetorno = resposta.jsonPath().getString("erros");

        assertTrue(mensagemRetorno.contains("campo.descricao.obrigatorio"));
        assertTrue(mensagemRetorno.contains("campo.username.obrigatorio"));
    }

    @Test
    @DisplayName("Deve criar uma Natureza com DTO preenchido")
    @Order(2)
    void criarNaturezaSucesso() {

        // 1. Cria o usuário primeiro
        UsuarioDTO dtoUser = new UsuarioDTO();
        dtoUser.setUsername(USERNAME_EXISTENTE);

        var respostaUser = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoUser)
                .when()
                .post("/usuarios")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String usernameCriado = respostaUser.jsonPath().getString(TAG_USERNAME);
        LOG.info("Usuário criado ::: " + usernameCriado);


        // 2. Depois que salvar o usuário, cria a natureza
        NaturezaNewDTO dtoNatureza = new NaturezaNewDTO();
        dtoNatureza.setDescricao("Natureza de Exemplo");
        dtoNatureza.setUsername(usernameCriado);

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoNatureza)
                .when()
                .post("/naturezas")
                .then()
                .statusCode(201)
                .extract()
                .response();

        String responseBody = resposta.getBody().asString();
        LOG.info(responseBody);

        assertEquals(dtoNatureza.getUsername(), resposta.jsonPath().getString(TAG_USERNAME));
        assertEquals(dtoNatureza.getDescricao(), resposta.jsonPath().getString("descricao"));
    }


}