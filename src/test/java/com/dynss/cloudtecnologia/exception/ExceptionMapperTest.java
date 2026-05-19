package com.dynss.cloudtecnologia.exception;

import com.dynss.cloudtecnologia.exception.mapper.GeralExceptionMapper;
import com.dynss.cloudtecnologia.exception.mapper.JaExisteNaturezaCadastradaParaUsernameMapper;
import com.dynss.cloudtecnologia.rest.dto.ErrosResponseDTO;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class ExceptionMapperTest {

    @Test
    @DisplayName("Deve criar mensagens das excecoes")
    void deveCriarMensagensExcecoes() {
        assertTrue(new EntidadeNaoEncontradaException("Usuario", "id", "1", 404).getMessage().contains("Usuario"));
        assertEquals("mensagem", new GeralException("mensagem").getMessage());
        assertTrue(new JaExisteNaturezaCadastradaParaUsername().getMessage().contains("natureza"));
        assertTrue(new LancamentoNaoEncontradoException().getMessage().contains("Lan"));
        assertTrue(new NaturezaNaoEncontrada().getMessage().contains("Natureza"));
        assertTrue(new UsuarioNaoEncontradoException().getMessage().contains("Usu"));
    }

    @Test
    @DisplayName("Deve mapear excecao geral")
    void deveMapearExcecaoGeral() {
        Response response = new GeralExceptionMapper().toResponse(new GeralException("erro geral"));

        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
        ErrosResponseDTO entity = (ErrosResponseDTO) response.getEntity();
        assertEquals("erro geral", entity.getErros().getFirst());
    }

    @Test
    @DisplayName("Deve mapear natureza duplicada")
    void deveMapearNaturezaDuplicada() {
        Response response = new JaExisteNaturezaCadastradaParaUsernameMapper()
                .toResponse(new JaExisteNaturezaCadastradaParaUsername());

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        ErrosResponseDTO entity = (ErrosResponseDTO) response.getEntity();
        assertTrue(entity.getErros().getFirst().contains("natureza"));
    }
}
