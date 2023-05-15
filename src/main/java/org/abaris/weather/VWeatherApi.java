package org.abaris.weather;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

import static java.lang.Double.*;

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
            HttpRequest<Buffer> meteoReq = WebClient.create(vertx, new WebClientOptions().setTrustAll(true))
                    .get(port, host, "/v1/forecast")
                    .addQueryParam("latitude", "51.51")
                    .addQueryParam("longitude", "-0.13")
                    .addQueryParam("hourly", "temperature_2m")
                    .addQueryParam("forecast_days", "1");
//                    .ssl(true);
            meteoReq
                    .send()
                    .onSuccess(openMeteoResponse -> {
                        System.out.println(openMeteoResponse.bodyAsString());
                                                var jsonObject = openMeteoResponse.bodyAsJsonObject();

                                                request.response().end(
                                                        new JsonObject().put("latitude", jsonObject.getDouble("latitude"))
                                                                .put("longitude", jsonObject.getDouble("longitude"))
                                                                .put("temperatures", jsonObject.getJsonObject("hourly").getJsonArray("temperature_2m").stream()
                                                                        .map(obj -> parseDouble(obj.toString()))
                                                                        .toList()
                                                                ).encode());
                                            });
        };
    }
}
