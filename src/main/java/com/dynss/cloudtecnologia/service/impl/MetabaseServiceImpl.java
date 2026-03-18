package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.metabase.MetabaseEmbedResponseDTO;
import com.dynss.cloudtecnologia.service.MetabaseService;
import javax.crypto.SecretKey;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import com.dynss.cloudtecnologia.service.UsuarioService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;



@ApplicationScoped
public class MetabaseServiceImpl implements MetabaseService {

    private static final String ID_USER = "id_usuario";

    @Inject
    private UsuarioService usuarioService;

    @ConfigProperty(name = "metabase.secret-key")
    String metabaseSecretKey;

    @ConfigProperty(name = "metabase.dashboard-id")
    Integer dashboardId;

    @ConfigProperty(name = "metabase.instance-url")
    String metabaseInstanceUrl;


    @Override
    public MetabaseEmbedResponseDTO gerarTokenDashboard() {
        Usuario userLogado = usuarioService.findByTokenJwt();

        SecretKey key = Keys.hmacShaKeyFor(
                metabaseSecretKey.getBytes(StandardCharsets.UTF_8)
        );

        final String token = Jwts.builder()
                .claim("resource", Map.of("dashboard", dashboardId))
                .claim("params", Map.of(ID_USER, userLogado.getId()))
                .claim("_embedding_params", Map.of(
                        ID_USER, "locked",
                        "data_lancamento", "enabled"
                ))
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusSeconds(600)))
                .signWith(key, Jwts.SIG.HS256)
                .compact();

        return new MetabaseEmbedResponseDTO(token, metabaseInstanceUrl);
    }


}
