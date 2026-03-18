package com.dynss.cloudtecnologia.rest.controller;


import com.dynss.cloudtecnologia.rest.dto.metabase.MetabaseEmbedResponseDTO;
import com.dynss.cloudtecnologia.service.MetabaseService;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


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
