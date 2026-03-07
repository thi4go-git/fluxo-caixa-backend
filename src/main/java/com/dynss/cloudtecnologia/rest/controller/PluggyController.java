package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.service.PluggyItemService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;
import static com.dynss.cloudtecnologia.constants.ControllerConstants.SERVER_ERROR;



@Path("/pluggy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pluggy Item Controller", description = "API de Item Pluggy")
public class PluggyController {


    @Inject
    private PluggyItemService pluggyItemService;



    @PATCH
    @Path("/items/{id}")
    @Operation(summary = "Atualiza trancações do item pelo id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200",
                description = "Atuaizar transaçẽs de um item"
            ),
            @APIResponse(responseCode = "500", description = SERVER_ERROR),
    })
    public Response atualizarTransacoesItem(
            @PathParam("id") @Parameter(required = true, example = "5da12074-d868-4827-a540-051234567890")
            @NotNull(message = "id é obrigatório")
            final UUID id,
            @HeaderParam("x-apikey")
            @NotBlank(message = "xapikey é obrigatório")
            final String xapikey
    ) {
        Object status = pluggyItemService.atualizarTransacoesItem(id, xapikey);
        return Response.ok(status).build();
    }



    @POST
    @Path("/webhooks")
    public Response receberWebhook(
            Object payload,
            @HeaderParam("Authorization") String authorization
    ) {
//        if (authorization == null || !authorization.equals("Bearer teste123456789")) {
//            return Response.status(Response.Status.UNAUTHORIZED).build();
//        }

        System.out.println("WEBHOOK RECEBIDO");
        System.out.println(payload);

        return Response.ok().build();
    }

}
