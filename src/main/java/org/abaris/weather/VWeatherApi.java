package org.abaris.weather;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class VWeatherApi extends AbstractVerticle {

    private final int port;
    private final String host;

    public VWeatherApi(int port, String host) {
        this.port = port;
        this.host = host;
    }

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(handleWeatherRequest())
                .listen(8080);
    }
    private Handler<HttpServerRequest> handleWeatherRequest() {
        return request -> {
            var meteoReq = WebClient.create(vertx, new WebClientOptions().setTrustAll(true).setSsl(true).setVerifyHost(false))
                    .get(port, host, "/v1/forecast")
                    .addQueryParam("latitude", "51.51")
                    .addQueryParam("longitude", "-0.13")
                    .addQueryParam("current_weather", "true");

            meteoReq
                    .send()
                    .onSuccess(openMeteoResponse -> {
                        var jsonObject = openMeteoResponse.bodyAsJsonObject();

                        request.response().end(
                                new JsonObject().put("latitude", jsonObject.getDouble("latitude"))
                                        .put("longitude", jsonObject.getDouble("longitude"))
                                        .put("temperature", jsonObject.getJsonObject("current_weather").getDouble("temperature")
                                ).encode());
                    });
        };
    }
}
