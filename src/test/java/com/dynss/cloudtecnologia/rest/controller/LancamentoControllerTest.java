package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.model.enums.Origem;
import com.dynss.cloudtecnologia.model.enums.Situacao;
import com.dynss.cloudtecnologia.model.enums.TipoLancamento;
import com.dynss.cloudtecnologia.model.enums.TipoOperacaoLancamento;
import com.dynss.cloudtecnologia.rest.dto.LancamentoFilterDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoNewDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoUpdateDTO;
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
import jakarta.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
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

    private record CenarioLancamento(String username, String idNatureza, String descricaoNatureza) {
    }


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
        lancamentoNewDTO.setOrigem(Origem.PROPRIO);

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

    @Test
    @DisplayName("Deve listar lancamentos por periodo")
    @Order(3)
    void deveListarLancamentosPorPeriodo() {
        CenarioLancamento cenario = criaCenarioLancamento("07", "08", TipoLancamento.CREDITO, 1);

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, cenario.username())
                .queryParam("inicio", LocalDate.now().withDayOfMonth(1).toString())
                .queryParam("fim", LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth()).toString())
                .when()
                .get("/lancamentos")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(resposta.jsonPath().getInt("totalLancamentos") >= 1);
        assertEquals(DESCRICAO_LANCAMENTO_EXISTENTE, resposta.jsonPath().getString("lancamentos[0].descricao"));
    }

    @Test
    @DisplayName("Deve filtrar lancamentos")
    @Order(4)
    void deveFiltrarLancamentos() {
        CenarioLancamento cenario = criaCenarioLancamento("09", "10", TipoLancamento.CREDITO, 1);
        LancamentoFilterDTO filtro = new LancamentoFilterDTO();
        filtro.setUsername(cenario.username());
        filtro.setDescricao(DESCRICAO_LANCAMENTO_EXISTENTE);

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(filtro)
                .when()
                .post("/lancamentos/filter")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertTrue(resposta.jsonPath().getInt("totalLancamentos") >= 1);
    }

    @Test
    @DisplayName("Deve listar enums de lancamento")
    @Order(5)
    void deveListarEnumsLancamento() {
        given().when().get("/lancamentos/situacao").then().statusCode(HttpStatus.SC_OK);
        given().when().get("/lancamentos/tipo").then().statusCode(HttpStatus.SC_OK);
        given().when().get("/lancamentos/origem").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    @DisplayName("Deve retornar dashboard")
    @Order(6)
    void deveRetornarDashboard() {
        CenarioLancamento cenario = criaCenarioLancamento("11", "12", TipoLancamento.CREDITO, 1);

        var resposta = given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, cenario.username())
                .when()
                .get("/lancamentos/dashboard")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, resposta.statusCode());
        assertEquals(LocalDate.now().getYear(), resposta.jsonPath().getInt("ano"));
    }

    @Test
    @DisplayName("Deve buscar e atualizar lancamento")
    @Order(7)
    void deveBuscarEAtualizarLancamento() {
        CenarioLancamento cenario = criaCenarioLancamento("13", "14", TipoLancamento.CREDITO, 1);
        Long idLancamento = buscarPrimeiroLancamentoId(cenario.username());

        var respostaBusca = given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam(TAG_ID, idLancamento)
                .when()
                .get("/lancamentos/{id}")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, respostaBusca.statusCode());
        assertEquals(idLancamento.intValue(), respostaBusca.jsonPath().getInt(TAG_ID));

        LancamentoUpdateDTO updateDTO = new LancamentoUpdateDTO();
        updateDTO.setId(idLancamento);
        updateDTO.setUsername(cenario.username());
        updateDTO.setTipo(TipoLancamento.DEBITO);
        updateDTO.setDescricao("Lancamento atualizado");
        updateDTO.setDataLancamento(LocalDate.now());
        updateDTO.setValorParcela(new BigDecimal("123.45"));
        updateDTO.setNatureza(cenario.descricaoNatureza());
        updateDTO.setSituacao(Situacao.PAGO);
        updateDTO.setOrigem(Origem.TERCEIROS);

        var respostaUpdate = given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updateDTO)
                .when()
                .put("/lancamentos")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, respostaUpdate.statusCode());
        assertEquals("Lancamento atualizado", respostaUpdate.jsonPath().getString("descricao"));
        assertEquals("DEBITO", respostaUpdate.jsonPath().getString("tipo"));
    }

    @Test
    @DisplayName("Deve fazer upload e download de anexo")
    @Order(8)
    void deveFazerUploadEDownloadAnexo() {
        CenarioLancamento cenario = criaCenarioLancamento("15", "16", TipoLancamento.CREDITO, 1);
        Long idLancamento = buscarPrimeiroLancamentoId(cenario.username());

        given()
                .multiPart("anexo", "comprovante.txt", "conteudo".getBytes(), MediaType.TEXT_PLAIN)
                .multiPart("nome", "comprovante.txt")
                .multiPart("type", MediaType.TEXT_PLAIN)
                .when()
                .post("/lancamentos/{id}/upload", idLancamento)
                .then()
                .statusCode(HttpStatus.SC_OK);

        var respostaDownload = given()
                .contentType(MediaType.APPLICATION_JSON)
                .pathParam(TAG_ID, idLancamento)
                .when()
                .get("/lancamentos/{id}/download")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, respostaDownload.statusCode());
        assertEquals("comprovante.txt", respostaDownload.jsonPath().getString("nome"));
        assertEquals(MediaType.TEXT_PLAIN, respostaDownload.jsonPath().getString("type"));
    }

    @Test
    @DisplayName("Deve deletar lancamento por id")
    @Order(9)
    void deveDeletarLancamentoPorId() {
        CenarioLancamento cenario = criaCenarioLancamento("17", "18", TipoLancamento.CREDITO, 1);
        Long idLancamento = buscarPrimeiroLancamentoId(cenario.username());

        given()
                .pathParam(TAG_ID, idLancamento)
                .when()
                .delete("/lancamentos/{id}")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Deve deletar multiplos lancamentos")
    @Order(10)
    void deveDeletarMultiplosLancamentos() {
        CenarioLancamento cenario = criaCenarioLancamento("19", "20", TipoLancamento.CREDITO, 2);
        List<String> ids = buscarLancamentoIds(cenario.username());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, cenario.username())
                .body(ids)
                .when()
                .post("/lancamentos/deletar-multiplos")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    @DisplayName("Deve executar operacao personalizada para marcar como pago")
    @Order(11)
    void deveExecutarOperacaoPersonalizadaMarcarComoPago() {
        CenarioLancamento cenario = criaCenarioLancamento("21", "22", TipoLancamento.CREDITO, 1);
        Long idLancamento = buscarPrimeiroLancamentoId(cenario.username());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, cenario.username())
                .queryParam("tipoOperacao", TipoOperacaoLancamento.MARCAR_COMO_PAGO.getId())
                .body(List.of(String.valueOf(idLancamento)))
                .when()
                .post("/lancamentos/operacao-personalizada")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        var respostaBusca = given()
                .pathParam(TAG_ID, idLancamento)
                .when()
                .get("/lancamentos/{id}")
                .then()
                .extract()
                .response();

        assertEquals(HttpStatus.SC_OK, respostaBusca.statusCode());
        assertEquals("PAGO", respostaBusca.jsonPath().getString("situacao"));
    }

    @Test
    @DisplayName("Deve executar operacao personalizada para deletar")
    @Order(12)
    void deveExecutarOperacaoPersonalizadaDeletar() {
        CenarioLancamento cenario = criaCenarioLancamento("23", "24", TipoLancamento.CREDITO, 1);
        Long idLancamento = buscarPrimeiroLancamentoId(cenario.username());

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, cenario.username())
                .queryParam("tipoOperacao", TipoOperacaoLancamento.DELETAR.getId())
                .body(List.of(String.valueOf(idLancamento)))
                .when()
                .post("/lancamentos/operacao-personalizada")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
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

    private CenarioLancamento criaCenarioLancamento(String username, String natureza, TipoLancamento tipo, int qtdeParcelas) {
        criaUsuarioNatureza(username, natureza);

        LancamentoNewDTO lancamentoNewDTO = new LancamentoNewDTO();
        lancamentoNewDTO.setUsername(usernameCriado);
        lancamentoNewDTO.setIdNatureza(Long.parseLong(idNaturezaStr));
        lancamentoNewDTO.setDescricao(DESCRICAO_LANCAMENTO_EXISTENTE);
        lancamentoNewDTO.setValorTotal(new BigDecimal(2500));
        lancamentoNewDTO.setQtdeParcelas(qtdeParcelas);
        lancamentoNewDTO.setDataReferencia(LocalDate.now());
        lancamentoNewDTO.setOrigem(Origem.PROPRIO);
        lancamentoNewDTO.setTipo(tipo);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body(lancamentoNewDTO)
                .when()
                .post("/lancamentos")
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        return new CenarioLancamento(usernameCriado, idNaturezaStr, DESCRICAO_NATUREZA_EXISTENTE + natureza);
    }

    private Long buscarPrimeiroLancamentoId(String username) {
        return Long.valueOf(buscarLancamentoIds(username).getFirst());
    }

    private List<String> buscarLancamentoIds(String username) {
        return given()
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam(TAG_USERNAME, username)
                .when()
                .get("/lancamentos")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .getList("lancamentos.id", String.class);
    }


}
