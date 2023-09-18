package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.rest.dto.*;
import com.dynss.cloudtecnologia.service.impl.LancamentoServiceImpl;
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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Path("/lancamentos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Lançamento Controller", description = "Serviços para manutenção dos lançamentos")
@SecuritySchemes({
        @SecurityScheme(
                securitySchemeName = "lancamento-controller-oauth",
                type = SecuritySchemeType.OAUTH2,
                flows = @OAuthFlows(password = @OAuthFlow(tokenUrl = "http://cloudtecnologia.dynns.com:8180/realms/CLOUD_TECNOLOGIA/protocol/openid-connect/token"))
        )
})
@SecurityRequirement(name = "lancamento-controller-oauth")
public class LancamentoController {


    @Inject
    private LancamentoServiceImpl service;

    @POST
    @RequestBody(required = true)
    @Operation(summary = "Cria um Lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Lançamento criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LancamentoDTO.class))),
    })
    public Response save(
            @RequestBody(description = "DTO do Lançamento a ser criado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoDTO.class))) @Valid final LancamentoDTO dto ) {

        LancamentoDTO novo = service.lancar(dto);
        return Response
                .status(Response.Status.CREATED.getStatusCode())
                .entity(novo)
                .build();
    }


    @GET
    @Operation(summary = "Listar os Lançamentos por data")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Lista de Lançamentos por data",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LancamentoDataDTO.class)))
    })
    public Response finByIdUserData(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username,
            @QueryParam("inicio") @Parameter(example = "dd/MM/yyyy") String inicio,
            @QueryParam("fim") @Parameter(example = "dd/MM/yyyy") String fim) {

        if (inicio == null || fim == null) {
            LocalDate dataAtual = LocalDate.now();
            inicio = dataAtual.withDayOfMonth(1)
                    .format(DateTimeFormatter.ISO_DATE);
            fim = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth())
                    .format(DateTimeFormatter.ISO_DATE);
        }
        LancamentoDataDTO lancamentoFiltro = service.listarLancamentosByUsuarioDate(username, inicio, fim);
        return Response.ok(lancamentoFiltro).build();
    }


    @POST
    @Path("/filter") 
    @RequestBody(required = true)
    @Operation(summary = "Filtrar Lançamentos")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Filtro aplicado com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LancamentoDataDTO.class))),
    })
    public Response finByIdUserDataFilter(
            @RequestBody(description = "DTO do Filtro a ser aplicado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoDTO.class))) @Valid final LancamentoFilterDTO dtoFilter,
            @QueryParam("inicio") @Parameter(example = "dd/MM/yyyy") String inicio,
            @QueryParam("fim") @Parameter(example = "dd/MM/yyyy") String fim) {

        if (inicio == null || fim == null) {
            LocalDate dataAtual = LocalDate.now();
            inicio = dataAtual.withDayOfMonth(1)
                    .format(DateTimeFormatter.ISO_DATE);
            fim = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth())
                    .format(DateTimeFormatter.ISO_DATE);
        }
        dtoFilter.setData_inicio(inicio);
        dtoFilter.setData_fim(fim);

        LancamentoDataDTO lancamentoFiltro = service.listarLancamentosByUsuarioDateFilter(dtoFilter);
        return Response.ok(lancamentoFiltro).build();
    }


    @GET
    @Path("/situacao")
    @Operation(summary = "Listar tipos de Situação")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Tipos de Situações listadas com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response findAllSituacao() {
        return Response.ok(
                service.listarSituacao()).build();
    }


    @GET
    @Path("/tipo")
    @Operation(summary = "Listar tipos de Lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Tipos de Lançamento listados com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response findAllTipo() {
        return Response.ok(service.listarTipoLancamento()).build();
    }


    @GET
    @Path("/dashboard")
    @Operation(summary = "Listar os Lançamentos do ano corrente por username")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Lista de Lançamentos do ano corrente por username",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DashboardDTO.class)))
    })
    public Response getLancamentosDashboard(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username) {

        DashboardDTO response = service.getLancamentosDashboard(username);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar lançamento por id")
    @APIResponses(value = {
            @APIResponse(responseCode = "204",
                    description = "Lançamento deletado com Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response deleteById(@PathParam("id") Long id) {
        service.deleteById(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/deletar-multiplos")
    @Operation(summary = "Deletar Múltiplos lançamentos")
    @APIResponses(value = {
            @APIResponse(responseCode = "204",
                    description = "Lançamentos deletados com Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON))
    })
    public Response deletarMultiplosLancamentos(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username,
            @RequestBody(description = "Lista de Lançamentos para deletar", required = true) final List<String> lancamentosIds
    ) {
        service.deleteByIdList(lancamentosIds);
        return Response.noContent().build();
    }


    @PUT
    @RequestBody(required = true)
    @Operation(summary = "Atualizar lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lançamento atualizado com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LancamentoDataDTO.class))),
    })
    public Response update(
            @RequestBody(description = "DTO do Lançamento a ser atualizado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoDTO.class))) @Valid final LancamentoDTO dto) {

        LancamentoDTO dtoUpdate = service.update(dto);
        return Response.ok(dtoUpdate).build();
    }


}
