package com.dynss.cloudtecnologia.exception.mapper;

import com.dynss.cloudtecnologia.exception.BaseException;
import com.dynss.cloudtecnologia.rest.dto.ErrosResponseDTO;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class BaseExceptionMapper implements ExceptionMapper<BaseException> {
    private static final Logger LOGGER = Logger.getLogger(BaseExceptionMapper.class);

    @Override
    public Response toResponse(BaseException exception) {
        LOGGER.info(exception.getMessage());
        List<String> erros = new ArrayList<>();
        erros.add(exception.getMessage());
        return Response.status(exception.getStatusCode())
                .entity(new ErrosResponseDTO(erros))
                .build();
    }
}