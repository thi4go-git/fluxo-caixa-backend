package com.dynss.cloudtecnologia.exception.mapper;

import com.dynss.cloudtecnologia.rest.dto.ErrosResponseDTO;
import org.jboss.logging.Logger;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = Logger.getLogger(ConstraintViolationExceptionMapper.class);
    public static final int STATUS_CODE = 400;


    @Override
    public Response toResponse(ConstraintViolationException exception) {
        LOGGER.info(exception.getMessage());
        List<String> erros = new ArrayList<>();
        for (ConstraintViolation<?> excepti : exception.getConstraintViolations()) {
            erros.add(excepti.getMessage());
        }
        return Response.status(STATUS_CODE)
                .entity(new ErrosResponseDTO(erros))
                .build();
    }
}