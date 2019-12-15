package io.vertx.workshop.trader.impl;

import io.vertx.core.AbstractVerticle;


/**
 * The main verticle creating compulsive traders.
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() {
        vertx.deployVerticle(CallbackTraderVerticle.class.getName());
        System.out.println("The callback-based trader verticle deployed");
        vertx.deployVerticle(RXCompulsiveTraderVerticle.class.getName());
        System.out.println("The RX compulsive trader verticle deployed");        
    }

}
