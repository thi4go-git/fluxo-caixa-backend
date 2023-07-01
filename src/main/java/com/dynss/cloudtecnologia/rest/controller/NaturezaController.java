package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.exception.JaExisteNaturezaCadastradaParaUsername;
import com.dynss.cloudtecnologia.exception.UsuarioNaoEncontradoException;
import com.dynss.cloudtecnologia.model.entity.Natureza;
import com.dynss.cloudtecnologia.rest.dto.LancamentoDTO;
import com.dynss.cloudtecnologia.rest.dto.LancamentoDataDTO;
import com.dynss.cloudtecnologia.rest.dto.NaturezaDTO;
import com.dynss.cloudtecnologia.service.impl.NaturezaServiceImpl;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
@Tag(name = "Natureza Controller", description = "Serviços para manutenção das Naturezas")// Swagger
public class NaturezaController {

    @Inject
    private NaturezaServiceImpl naturezaService;


    @POST
    @RequestBody(required = true)
    @Operation(summary = "Cria uma Natureza")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Natureza criada com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = NaturezaDTO.class))),
    })
    public Response save(
            @RequestBody(description = "DTO da natureza a ser criada", required = true,
                    content = @Content(schema = @Schema(implementation = NaturezaDTO.class))) @Valid final NaturezaDTO dto)
            throws JaExisteNaturezaCadastradaParaUsername, UsuarioNaoEncontradoException {
        //
        NaturezaDTO novaNatureza = naturezaService.save(dto);
        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(novaNatureza)
                .build();
    }

    @GET
    @Operation(summary = "Listar  Naturezas")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Lista de Naturezas",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = NaturezaDTO.class)))
    })
    public Response getNaturezasByUsername(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username) {
        List<NaturezaDTO> response = naturezaService.getNaturezasByUsername(username);
        return Response.ok(response).build();
    }


}
