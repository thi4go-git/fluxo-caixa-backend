package com.dynss.cloudtecnologia.exception.mapper;

import com.dynss.cloudtecnologia.exception.GeralException;
import com.dynss.cloudtecnologia.rest.dto.ErrosConstraintViolationDTO;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class GeralExceptionMapper implements ExceptionMapper<GeralException> {

    private static final Logger LOGGER = Logger.getLogger(GeralExceptionMapper.class);


    @Override
    public Response toResponse(GeralException exception) {
        LOGGER.info(exception.getMessage());
        List<String> erros = new ArrayList<>();
        erros.add(exception.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrosConstraintViolationDTO(erros))
                .build();
    }
}
