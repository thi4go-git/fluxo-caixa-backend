package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.model.entity.Usuario;
import com.dynss.cloudtecnologia.rest.dto.UsuarioDTO;
import com.dynss.cloudtecnologia.rest.mapper.UsuarioMapper;
import com.dynss.cloudtecnologia.service.impl.UsuarioServiceImpl;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.*;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/usuarios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Usuário Controller", description = "API de Usuários")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = "usuario-controller-oauth",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://cloudtecnologia.dynns.com:8180/realms/CLOUD_TECNOLOGIA/protocol/openid-connect/token"))
        )
})
@SecurityRequirement(name = "usuario-controller-oauth")
public class UsuarioController {

    @Inject
    private UsuarioServiceImpl userService;

    @Inject
    private UsuarioMapper usuarioMapper;

    private static final String SERVER_ERROR = "Erro Interno no servidor.";
    private static final String USUARIO_NOTFOUND = "Usuário não encontrado.";

    @POST
    @RequestBody(required = true)
    @Operation(summary = "Cria um Usuário")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = UsuarioDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR)

    })
    public Response save(
            @RequestBody(description = "DTO do Usuário a ser criado", required = true,
                    content = @Content(schema = @Schema(implementation = UsuarioDTO.class)))
            @Valid final UsuarioDTO dto
    ) {
        Usuario novo = userService.save(dto);
        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(usuarioMapper.toDto(novo))
                .build();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Buscar Usuário por ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Buscar Usuário por ID",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsuarioDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = USUARIO_NOTFOUND)
    })
    public Response findById(
            @PathParam("id") @Parameter(required = true, example = "1") @NotNull(message = "id é obrigatório") final Long id
    ) {
        Usuario userAchado = userService.findById(id);
        return Response.ok(usuarioMapper.toDto(userAchado)).build();
    }


    @GET
    @Operation(summary = "Buscar Usuários ")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Listagem de Usuários",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = UsuarioDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public Response findAll() {
        List<Usuario> usuarios = userService.findAll();
        return Response.ok(usuarioMapper.listToDTO(usuarios)).build();
    }


}
