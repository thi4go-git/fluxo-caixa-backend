package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.metabase.MetabaseEmbedResponseDTO;
import com.dynss.cloudtecnologia.service.UsuarioService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
class MetabaseServiceImplTest {

    @Inject
    MetabaseServiceImpl service;

    @InjectMock
    UsuarioService usuarioService;

    @ConfigProperty(name = "metabase.instance-url")
    String metabaseInstanceUrl;

    @Test
    @DisplayName("Deve gerar token de dashboard")
    void deveGerarTokenDashboard() {
        Usuario usuario = new Usuario();
        usuario.setId(10L);
        usuario.setUsername("username");

        when(usuarioService.findByTokenJwt()).thenReturn(usuario);

        MetabaseEmbedResponseDTO resposta = service.gerarTokenDashboard();

        assertNotNull(resposta.getToken());
        assertFalse(resposta.getToken().isBlank());
        assertEquals(metabaseInstanceUrl, resposta.getInstanceUrl());
        verify(usuarioService).findByTokenJwt();
    }
}
