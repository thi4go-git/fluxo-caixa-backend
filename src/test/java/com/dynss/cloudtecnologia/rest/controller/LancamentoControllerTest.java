package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.rest.dto.LancamentoNewDTO;
import com.dynss.cloudtecnologia.rest.dto.NaturezaNewDTO;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LancamentoControllerTest {


    private static final Logger LOG = LoggerFactory.getLogger(LancamentoControllerTest.class);

    private static final String USERNAME_EXISTENTE = "username-lancamento";
    private static final String TAG_USERNAME = "username";
    private static final String DESCRICAO_NATUREZA_EXISTENTE = "Natureza de Exemplo Lançamento01";
    private static final String DESCRICAO_LANCAMENTO_EXISTENTE = "Exemplo Lançamento01";
    private static final String END_POINT_NATUREZAS = "/naturezas";
    private static final String END_POINT_USUARIOS = "/usuarios";
    private static final String TAG_ID = "id";

    private String usernameCriado;
    private String idNaturezaStr;


    @Test
    @DisplayName("Não Deve salvar uma Lancamento com DTO Vazio")
    @Order(1)
    @TestHTTPEndpoint(LancamentoController.class)
    void saveError() {

        LancamentoNewDTO dto = new LancamentoNewDTO();

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
        assertEquals(HttpStatus.SC_BAD_REQUEST, resposta.statusCode());

        assertTrue(responseBody.contains("campo.username.obrigatorio"));
        assertTrue(responseBody.contains("campo.descricao.obrigatorio"));
        assertTrue(responseBody.contains("campo.id_natureza.obrigatorio"));
        assertTrue(responseBody.contains("(username): Usuário não localizado!"));
        assertTrue(responseBody.contains("campo.qtde_parcelas.obrigatorio"));
    }


    // Método para fornecer os tipos de lançamento como argumentos para o teste parametrizado
    static Stream<TipoLancamento> tipoLancamentoProvider() {
        return Stream.of(TipoLancamento.DEBITO, TipoLancamento.CREDITO, TipoLancamento.AMBOS);
    }

    @ParameterizedTest
    @MethodSource("tipoLancamentoProvider")
    @DisplayName("Deve salvar um Lançamento Tipo Débito")
    @Order(2)
    void criaLancamentoDebito(TipoLancamento tipoLancamento) {

        LancamentoNewDTO lancamentoNewDTO = new LancamentoNewDTO();

        if (tipoLancamento == TipoLancamento.DEBITO) {
            criaUsuarioNatureza("01", "02");
            lancamentoNewDTO.setTipo(TipoLancamento.DEBITO);
        } else if (tipoLancamento == TipoLancamento.CREDITO) {
            criaUsuarioNatureza("03", "04");
            lancamentoNewDTO.setTipo(TipoLancamento.CREDITO);
        } else {
            criaUsuarioNatureza("05", "06");
            lancamentoNewDTO.setTipo(TipoLancamento.AMBOS);
        }

        lancamentoNewDTO.setUsername(usernameCriado);
        lancamentoNewDTO.setIdNatureza(Long.parseLong(idNaturezaStr));
        lancamentoNewDTO.setDescricao(DESCRICAO_LANCAMENTO_EXISTENTE);
        lancamentoNewDTO.setValorTotal(new BigDecimal(2500));
        lancamentoNewDTO.setQtdeParcelas(5);
        lancamentoNewDTO.setDataReferencia(LocalDate.now());

        var respostaLancamento = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(lancamentoNewDTO)
                .when()
                .post("/lancamentos")
                .then()
                .extract()
                .response();

        LOG.info("Lançamento criado ::: " + respostaLancamento.getBody().asString());
        assertEquals(HttpStatus.SC_CREATED, respostaLancamento.statusCode());
        assertEquals(usernameCriado, respostaLancamento.jsonPath().getString(TAG_USERNAME));
        assertTrue(respostaLancamento.jsonPath().getString("idNatureza").contains(idNaturezaStr));
        assertTrue(respostaLancamento.jsonPath().getString("descricao").contains(DESCRICAO_LANCAMENTO_EXISTENTE));
        assertTrue(respostaLancamento.jsonPath().getString("qtdeParcelas").contains("5"));

        if (tipoLancamento == TipoLancamento.DEBITO) {
            assertTrue(respostaLancamento.jsonPath().getString("tipo").contains("DEBITO"));
            assertTrue(respostaLancamento.jsonPath().getString("valorTotal").contains("-2500"));
        } else if (tipoLancamento == TipoLancamento.CREDITO) {
            assertTrue(respostaLancamento.jsonPath().getString("tipo").contains("CREDITO"));
            assertTrue(respostaLancamento.jsonPath().getString("valorTotal").contains("2500"));
        }
    }


    private void criaUsuarioNatureza(String username, String natureza) {
        UsuarioDTO dtoUser = new UsuarioDTO();
        dtoUser.setUsername(USERNAME_EXISTENTE + username);

        var respostaUser = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoUser)
                .when()
                .post(END_POINT_USUARIOS)
                .then()
                .statusCode(201)
                .extract()
                .response();

        usernameCriado = respostaUser.jsonPath().getString(TAG_USERNAME);

        //Cria natureza
        NaturezaNewDTO dtoNatureza = new NaturezaNewDTO();
        dtoNatureza.setDescricao(DESCRICAO_NATUREZA_EXISTENTE + natureza);
        dtoNatureza.setUsername(usernameCriado);

        var respostaNatureza = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(dtoNatureza)
                .when().post(END_POINT_NATUREZAS)
                .then()
                .statusCode(201)
                .extract()
                .response();

        idNaturezaStr = respostaNatureza.jsonPath().getString(TAG_ID);
    }


}