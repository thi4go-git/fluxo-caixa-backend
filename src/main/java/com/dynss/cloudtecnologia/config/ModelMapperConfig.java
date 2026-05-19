package com.dynss.cloudtecnologia.config;


import org.modelmapper.ModelMapper;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelMapperConfig {

    private ModelMapperConfig() {
    }

    public static ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
