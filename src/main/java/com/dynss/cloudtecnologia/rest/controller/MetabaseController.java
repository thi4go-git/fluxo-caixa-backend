package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.rest.dto.metabase.MetabaseEmbedResponseDTO;
import com.dynss.cloudtecnologia.service.MetabaseService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/metabase")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Tag(name = "Metabase Controller", description = "API Metabase")
public class MetabaseController {

    @Inject
    private MetabaseService metabaseService;


    @GET
    @Path("/dashboard-url-token")
    public Response getDashboardToken() {
        MetabaseEmbedResponseDTO metabaseTokenUrl = metabaseService.gerarTokenDashboard();
        return Response.ok(metabaseTokenUrl).build();
    }


}
