package com.dynss.cloudtecnologia.scheduler;


import com.dynss.cloudtecnologia.service.PluggyService;
import io.quarkus.scheduler.Scheduled;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;




@ApplicationScoped
public class AtualizarTransacoesPluggyJob {

    @ConfigProperty(name = "pluggy.webhook.itemIdConta")
    String itemIdConta;

    @Inject
    private PluggyService pluggyService;


    @Scheduled(every = "1h", concurrentExecution = Scheduled.ConcurrentExecution.SKIP)
    void atualizarTransacoesPluggy() {
        pluggyService.atualizarTransacoesItem(itemIdConta);
    }

}
