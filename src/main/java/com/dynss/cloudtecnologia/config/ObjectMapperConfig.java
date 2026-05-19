package com.dynss.cloudtecnologia.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ObjectMapperConfig {

    private ObjectMapperConfig() {
    }

    public static ObjectMapper getObjectMapper() {
        return new ObjectMapper();
    }
}
