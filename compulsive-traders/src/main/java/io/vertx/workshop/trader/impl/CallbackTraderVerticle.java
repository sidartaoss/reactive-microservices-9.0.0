package io.vertx.workshop.trader.impl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.MessageSource;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.workshop.portfolio.PortfolioService;

/**
 * A compulsive trader developed with callbacks and futures.
 */
public class CallbackTraderVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> done) throws Exception {
        
        String company = TraderUtils.pickACompany();

        int numberOfShares = TraderUtils.pickANumber();

        System.out.println("Java-Callback compulsive trader configured for company " + company + " and shares " + numberOfShares);

        // TODO: Complete the code to apply the trading logic on each message received from the "market-data" message source

        // ---

        // Retrieve service discovery
        Future<ServiceDiscovery> retrieveServiceDiscovery = getServiceDiscovery(vertx);

        // When the service discovery is retrieved, retrieve the portfolio service and market data
        retrieveServiceDiscovery.setHandler(discovery -> {
            // TODO 1 - Get the Future objects for the portfolio and market services. Just use the methods given below

            // TODO 2 - Use CompositeFuture.all to "wait" until both future are completed

            // TODO 3 - Attach a handler on the composite future, and call initialize

            Future<PortfolioService> retrieveThePortfolioService = getPortfolioService(discovery.result());
            Future<MessageConsumer<JsonObject>> retrieveTheMarket = getMarketSource(discovery.result());

            CompositeFuture.all(retrieveServiceDiscovery, retrieveTheMarket)
                .setHandler(ar -> {
                    initialize(done, 
                            company, 
                            numberOfShares, 
                            retrieveThePortfolioService, 
                            retrieveTheMarket, 
                            ar);
                });



        });
        

    }

    private Future<ServiceDiscovery> getServiceDiscovery(Vertx vertx) {
        Future<ServiceDiscovery> future = Future.future();
        ServiceDiscovery.create(vertx, future::complete);
        return future;
    }

    private Future<PortfolioService> getPortfolioService(
        ServiceDiscovery discovery) {
        Future<PortfolioService> future = Future.future();
        EventBusService.getServiceProxy(
            discovery, 
            record -> record.getName().equalsIgnoreCase("portfolio"), 
            PortfolioService.class, 
            future);
        return future;
    }

    private Future<MessageConsumer<JsonObject>> getMarketSource(ServiceDiscovery discovery) {
        Future<MessageConsumer<JsonObject>> future = Future.future();
        MessageSource.getConsumer(discovery,
                record -> record.getName().equalsIgnoreCase("market-data"),
                future
        );
        return future;
    }

    private void initialize(
        Future<Void> done,
        String company,
        int numberOfShares,
        Future<PortfolioService> retrieveThePortfolioSerivce,
        Future<MessageConsumer<JsonObject>> retrieveTheMarket,
        AsyncResult<CompositeFuture> ar
    ) {

        if (ar.failed()) {
            done.fail(ar.cause());
        } else {
            PortfolioService portfolio = retrieveThePortfolioSerivce.result();
            MessageConsumer<JsonObject> consumer = retrieveTheMarket.result();
            consumer.handler(
                message -> TraderUtils.dumbTradingLogic(company, numberOfShares, portfolio, message.body()));
            done.complete();
        }

    }

}