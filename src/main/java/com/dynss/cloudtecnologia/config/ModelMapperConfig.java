package com.dynss.cloudtecnologia.config;


import org.modelmapper.ModelMapper;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ModelMapperConfig {
    public static ModelMapper getModelMapper() {
        return new ModelMapper();
    }
}
