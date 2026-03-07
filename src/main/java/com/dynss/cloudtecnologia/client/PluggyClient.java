package com.dynss.cloudtecnologia.client;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;



@Path("/items")
@RegisterRestClient(configKey = "pluggy-api")
@Produces(MediaType.APPLICATION_JSON)
public interface PluggyClient {

    @PATCH
    @Path("/{id}")
    Object patchItem(
            @PathParam("id") String id,
            @HeaderParam("X-API-KEY") String apiKey
    );
}