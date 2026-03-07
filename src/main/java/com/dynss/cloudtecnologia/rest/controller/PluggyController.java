package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyWebhookDTO;
import com.dynss.cloudtecnologia.service.PluggyService;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;



@Path("/pluggy")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Pluggy Item Controller", description = "API de Item Pluggy")
public class PluggyController {


    @ConfigProperty(name = "pluggy.webhook.secret")
    String webhookSecret;

    @Inject
    private PluggyService pluggyService;


    @POST
    @Path("/webhooks")
    public Response receberWebhook(
            @RequestBody(description = "Payload do webhoow pluggy", required = true) final PluggyWebhookDTO payload,
            @HeaderParam("Authorization") String authorization
    ) {
        if (authorization == null || !authorization.equals(webhookSecret)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        pluggyService.receberWebhook(payload);
        return Response.ok().build();
    }

}
