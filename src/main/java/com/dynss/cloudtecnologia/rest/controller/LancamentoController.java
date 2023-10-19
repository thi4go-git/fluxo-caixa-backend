package com.dynss.cloudtecnologia.rest.controller;

import com.dynss.cloudtecnologia.model.entity.Lancamento;
import com.dynss.cloudtecnologia.rest.dto.*;
import com.dynss.cloudtecnologia.rest.mapper.AnexoMapper;
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
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    private LancamentoServiceImpl lancamentoService;

    @Inject
    private AnexoMapper anexoMapper;


    private static final String SERVER_ERROR = "Erro Interno do Servidor.";

    private static final String USER_NOTFOUND = "Usuário com o username informado não localizado.";

    private static final String LANCAMENTO_NOTFOUND = "Lançamento não localizado.";


    @POST
    @RequestBody(required = true)
    @Operation(summary = "Cria um Lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "201", description = "Lançamento criado com sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LancamentoNewDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response save(
            @RequestBody(description = "DTO do Lançamento a ser criado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoNewDTO.class))) @Valid final LancamentoNewDTO dto
    ) {
        LancamentoNewDTO novo = lancamentoService.lancar(dto);
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
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = LancamentoDataDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = USER_NOTFOUND),
    })
    public Response finByIdUserData(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username,
            @QueryParam("inicio") @Parameter(example = "dd/MM/yyyy") String inicio,
            @QueryParam("fim") @Parameter(example = "dd/MM/yyyy") String fim
    ) {
        if (inicio == null || fim == null) {
            LocalDate dataAtual = LocalDate.now();
            inicio = dataAtual.withDayOfMonth(1)
                    .format(DateTimeFormatter.ISO_DATE);
            fim = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth())
                    .format(DateTimeFormatter.ISO_DATE);
        }
        LancamentoDataDTO lancamentoFiltro = lancamentoService.listarLancamentosByUsuarioDate(username, inicio, fim);
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
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response finByIdUserDataFilter(
            @RequestBody(description = "DTO do Filtro a ser aplicado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoNewDTO.class))) @Valid final LancamentoFilterDTO dtoFilter,
            @QueryParam("inicio") @Parameter(example = "dd/MM/yyyy") String inicio,
            @QueryParam("fim") @Parameter(example = "dd/MM/yyyy") String fim
    ) {
        if (inicio == null || fim == null) {
            LocalDate dataAtual = LocalDate.now();
            inicio = dataAtual.withDayOfMonth(1)
                    .format(DateTimeFormatter.ISO_DATE);
            fim = dataAtual.withDayOfMonth(dataAtual.lengthOfMonth())
                    .format(DateTimeFormatter.ISO_DATE);
        }
        dtoFilter.setDataInicio(inicio);
        dtoFilter.setDataFim(fim);

        LancamentoDataDTO lancamentoFiltro = lancamentoService.listarLancamentosByUsuarioDateFilter(dtoFilter);
        return Response.ok(lancamentoFiltro).build();
    }


    @GET
    @Path("/situacao")
    @Operation(summary = "Listar tipos de Situação")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Tipos de Situações listadas com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response findAllSituacao() {
        return Response.ok(
                lancamentoService.listarSituacao()).build();
    }


    @GET
    @Path("/tipo")
    @Operation(summary = "Listar tipos de Lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Tipos de Lançamento listados com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response findAllTipo() {
        return Response.ok(lancamentoService.listarTipoLancamento()).build();
    }


    @GET
    @Path("/dashboard")
    @Operation(summary = "Listar os Lançamentos do ano corrente por username")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                    description = "Lista de Lançamentos do ano corrente por username",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = DashboardDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = USER_NOTFOUND),
    })
    public Response getLancamentosDashboard(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username
    ) {
        DashboardDTO response = lancamentoService.getLancamentosDashboard(username);
        return Response.ok(response).build();
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Deletar lançamento por id")
    @APIResponses(value = {
            @APIResponse(responseCode = "204",
                    description = "Lançamento deletado com Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = LANCAMENTO_NOTFOUND),
    })
    public Response deleteById(
            @PathParam("id") @NotNull(message = "O campo id é obrigatório!") final Long id
    ) {
        lancamentoService.deleteById(id);
        return Response.noContent().build();
    }

    @POST
    @Path("/deletar-multiplos")
    @Operation(summary = "Deletar Múltiplos lançamentos")
    @APIResponses(value = {
            @APIResponse(responseCode = "204",
                    description = "Lançamentos deletados com Sucesso",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response deletarMultiplosLancamentos(
            @QueryParam("username") @Parameter(required = true, example = "123.user") @NotBlank(message = "username é obrigatório") final String username,
            @RequestBody(description = "Lista de Lançamentos para deletar", required = true) final List<String> lancamentosIds
    ) {
        lancamentoService.deleteByIdList(lancamentosIds);
        return Response.noContent().build();
    }


    @PUT
    @RequestBody(required = true)
    @Operation(summary = "Atualizar lançamento")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Lançamento atualizado com Sucesso!",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON,
                            schema = @Schema(implementation = LancamentoNewDTO.class))),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = USER_NOTFOUND),
    })
    public Response update(
            @RequestBody(description = "DTO do Lançamento a ser atualizado", required = true,
                    content = @Content(schema = @Schema(implementation = LancamentoNewDTO.class))) @Valid final LancamentoNewDTO dto
    ) {
        LancamentoNewDTO dtoUpdate = lancamentoService.update(dto);
        return Response.ok(dtoUpdate).build();
    }


    @POST
    @Path("/{id}/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Operation(summary = "Upload de anexo")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Arquivo anexado!"),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = LANCAMENTO_NOTFOUND),
    })
    public Response uploadFile(
            @MultipartForm AnexoUploaDTO anexoUploaDTO,
            @PathParam("id") @NotNull(message = "O campo id é obrigatório!") final Long idLancamento
    ) {
        lancamentoService.uploadAnexo(anexoUploaDTO, idLancamento);
        return Response.ok().build();
    }


    @GET
    @Path("/{id}/download")
    @Operation(summary = "Download de anexo")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Arquivo Baixado!"),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
            @APIResponse(responseCode = "404", description = LANCAMENTO_NOTFOUND),
    })
    public Response downloadFile(
            @PathParam("id") @NotNull(message = "O campo id é obrigatório!") final Long idLancamento
    ) {
        Lancamento lancamento = lancamentoService.findByIdOrThrow(idLancamento);
        return Response.ok(anexoMapper.lancamentoToAnexoDownloaDTO(lancamento)).build();
    }

}
