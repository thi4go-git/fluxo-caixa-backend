package com.dynss.cloudtecnologia.service.impl;

import com.dynss.cloudtecnologia.client.PluggyClient;
import com.dynss.cloudtecnologia.service.PluggyItemService;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;
import javax.inject.Inject;


@ApplicationScoped
public class PluggyItemServiceImpl implements PluggyItemService {

    @Inject
    @RestClient
    private PluggyClient pluggyClient;

    @Override
    public Object atualizarTransacoesItem(final UUID id, final String xapikey) {
        return pluggyClient.patchItem(id.toString(), xapikey);
    }


}
