package com.dynss.cloudtecnologia.exception.mapper;

import com.dynss.cloudtecnologia.exception.JaExisteNaturezaCadastradaParaUsername;
import com.dynss.cloudtecnologia.rest.dto.ErrosConstraintViolationDTO;
import org.jboss.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.ArrayList;
import java.util.List;

@Provider
public class JaExisteNaturezaCadastradaParaUsernameMapper implements ExceptionMapper<JaExisteNaturezaCadastradaParaUsername> {


    private static final Logger LOGGER = Logger.getLogger(JaExisteNaturezaCadastradaParaUsernameMapper.class);

    @Override
    public Response toResponse(JaExisteNaturezaCadastradaParaUsername exception) {
        LOGGER.info(exception.getMessage());
        List<String> erros = new ArrayList<>();
        erros.add(exception.getMessage());

        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrosConstraintViolationDTO(erros))
                .build();
    }
}
