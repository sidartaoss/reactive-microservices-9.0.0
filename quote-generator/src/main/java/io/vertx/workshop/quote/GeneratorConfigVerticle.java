package io.vertx.workshop.quote;

import io.reactivex.Observable;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.config.ConfigRetriever;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.servicediscovery.ServiceDiscovery;
import io.vertx.reactivex.servicediscovery.types.MessageSource;
import io.vertx.servicediscovery.Record;

public class GeneratorConfigVerticle extends AbstractVerticle {

    /**
     * The address on which the data are sent
     */
    static final String ADDRESS = "market";

    private Record record;
    private ServiceDiscovery discovery;

    /**
     * This method is called when the verticle is deployed
     */
    @Override
    public void start(Future<Void> future) {

        discovery = ServiceDiscovery.create(vertx);
        ConfigRetriever retriever = ConfigRetriever.create(vertx, getConfigurationOptions());

        retriever.rxGetConfig()
            // Read the configuration, and deploy a MarketDataVerticle for each company listed in the configuration.
            .flatMap(config -> 
                Observable.fromIterable(config.getJsonArray("companies"))
                    .cast(JsonObject.class)
                    // Deploy the verticle with a configuration
                    .flatMapSingle(company -> vertx.rxDeployVerticle(MarketDataVerticle.class.getName(),
                        new DeploymentOptions().setConfig(company)))
                    .toList()
            )
            // Deploy another verticle
            .flatMap(l -> vertx.rxDeployVerticle(RestQuoteAPIVerticle.class.getName()))
            // Expose the market-data message source
            .flatMap(x -> discovery.rxPublish(MessageSource.createRecord("market-data", ADDRESS)))
            .subscribe((rec, err) -> {
                if (rec != null) {
                    this.record = rec;
                    future.complete();
                } else {
                    future.fail(err);
                }
            });
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        if (this.record != null) {
            this.discovery.rxUnpublish(this.record.getRegistration())
                .subscribe(CompletableHelper.toObserver(stopFuture));
        } else {
            stopFuture.complete();
        }
    }

    private ConfigRetrieverOptions getConfigurationOptions() {
        JsonObject path = new JsonObject().put("path", System.getenv("CONTAINER_PATH"));
        return new ConfigRetrieverOptions()
            .addStore(new ConfigStoreOptions().setType("file").setConfig(path));

    }

}
