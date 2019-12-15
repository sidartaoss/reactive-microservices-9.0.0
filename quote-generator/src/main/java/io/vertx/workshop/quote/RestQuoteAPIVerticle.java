package io.vertx.workshop.quote;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.reactivex.CompletableHelper;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.eventbus.Message;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.core.http.HttpServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * This verticle exposes a HTTP endpoint to retrieve the current / last values of the market data (quotes)
 */
public class RestQuoteAPIVerticle extends AbstractVerticle {

    private Map<String, JsonObject> quotes = new HashMap<>();
    
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        
        // Get (consume) the stream of messages sent on the "market" address.
        vertx.eventBus().<JsonObject>consumer(GeneratorConfigVerticle.ADDRESS).toFlowable()
        // TODO: Extract the body of the message using `.map(msg -> {})`
        //-----

            .map(Message::body)
        //-----
        // TODO: For each message, populate the `quotes` map with the received quote. Use  
        // `.doOnNext(json -> {})
        // Quotes are json objects you can retrieve from the message body
        // The map is structured as follows: name -> quote
        // -----


            .doOnNext(json -> {
                quotes.put(json.getString("name"), json);
            })

        //-----
        .subscribe();

        HttpServer server = vertx.createHttpServer();
        server.requestStream().toFlowable()
                .doOnNext(request -> {
                    HttpServerResponse response = request.response()          // 1. Get the response object from the request
                            .putHeader("content-type", "application/json");


                    // TODO: Handle the Http request
                    // The request handler returns a specific quote if the `name` parameter is set, or the whole map if none.
                    // To write the response use: `response.end(content)`
                    // If the name is set but not found you should return 404 (use response.setStatusCode(404)).
                    // To encode a Json object, use the `encodePrettily` method.
                    // -----

                        
                    String company = request.getParam("name");                 // 2. Gets the name parameter (query parameter) 

                    if (company == null) {
                        String content = Json.encodePrettily(quotes);          // 3. Encode the map to JSON
                        response.end(content);                                 // 4. Write the response and flush it using end(...)
                    } else {
                        JsonObject quote = quotes.get(company);
                        if (quote == null) {
                            response.setStatusCode(404).end();                  // 5. If the given named does not match a company, set the status code to 404
                        } else {
                            response.end(quote.encodePrettily());
                        }
                    }
                    
                })

        .subscribe();                                                           // 6. Notice the subscribe here. Without it, we would not get the requests

        server.rxListen(config().getInteger("http.port", 8080))
                .toCompletable()
                .subscribe(CompletableHelper.toObserver(startFuture));


    }
}