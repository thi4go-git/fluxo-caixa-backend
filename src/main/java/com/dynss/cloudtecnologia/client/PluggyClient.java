package com.dynss.cloudtecnologia.client;

import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthRequestDTO;
import com.dynss.cloudtecnologia.rest.dto.pluggy.PluggyAuthResponseDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;



@RegisterRestClient(configKey = "pluggy-api")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PluggyClient {


    @POST
    @Path("/auth")
    PluggyAuthResponseDTO auth(final PluggyAuthRequestDTO request);


    @PATCH
    @Path("/items/{id}")
    Object patchItem(
            @PathParam("id") String id,
            @HeaderParam("X-API-KEY") String apiKey
    );

    @GET
    @Path("/transactions")
    Object getTransactions(
           @HeaderParam("X-API-KEY") final String apiKey,
           @QueryParam("accountId") final String accountId,
           @QueryParam("from") final String from,
           @QueryParam("to") final String to
    );
}