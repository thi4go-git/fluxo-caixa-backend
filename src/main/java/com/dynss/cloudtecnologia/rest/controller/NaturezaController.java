package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.exception.JaExisteNaturezaCadastradaParaUsername;
import com.dynss.cloudtecnologia.exception.UsuarioNaoEncontradoException;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.rest.dto.NaturezaNewDTO;
import com.dynss.cloudtecnologia.rest.mapper.NaturezaMapper;
import com.dynss.cloudtecnologia.service.impl.NaturezaServiceImpl;
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
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/naturezas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Natureza Controller", description = "Serviços para manutenção das Naturezas")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = "natureza-controller-oauth",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://cloudtecnologia.dynns.com:8180/realms/CLOUD_TECNOLOGIA/protocol/openid-connect/token"))
        )
})
@SecurityRequirement(name = "natureza-controller-oauth")
public class NaturezaController {

    @Inject
    private NaturezaServiceImpl naturezaService;

    @Inject
    private NaturezaMapper naturezaMapper;


    private static final String SERVER_ERROR = "Erro Interno do Servidor.";


    @POST
    @RequestBody(required = true)
    @Operation(summary = "Cria uma Natureza")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Natureza criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = NaturezaNewDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR)
    })
    public Response save(
            @RequestBody(description = "DTO da natureza a ser criada", required = true,
                    content = @Content(schema = @Schema(implementation = NaturezaNewDTO.class))) @Valid final NaturezaNewDTO dto)
            throws JaExisteNaturezaCadastradaParaUsername, UsuarioNaoEncontradoException {

        Natureza novaNatureza = naturezaService.save(dto);
        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(naturezaMapper.toDto(novaNatureza))
                .build();
    }

    @GET
    @Operation(summary = "Listar  Naturezas")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Lista de Naturezas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = NaturezaNewDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = "username não localizado.")
    })
    public Response getNaturezasByUsername(
            @QueryParam("username") @Parameter(required = true, example = "user.saulo") @NotBlank(message = "QueryParam username é obrigatório") final String username
    ) {
        List<Natureza> naturezas = naturezaService.getNaturezasByUsername(username);
        return Response.ok(naturezaMapper.listNaturezaTolistDTO(naturezas)).build();
    }

    @DELETE
    @Operation(summary = "Deleta Natureza Cliente pelo ID")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Natureza Deletada", content = @Content(mediaType = MediaType.APPLICATION_JSON)), @APIResponse(responseCode = "204", description = "Deletado", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = "Natureza não localizada.")
    })
    public Response deletarNaturezaPorId(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username,
            @QueryParam("descricaoNatureza") @Parameter(required = true, example = "45") @NotBlank(message = "descricaoNatureza é obrigatório") final String descricaoNatureza
    ) {
        naturezaService.deletarNatureza(username, descricaoNatureza);
        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
