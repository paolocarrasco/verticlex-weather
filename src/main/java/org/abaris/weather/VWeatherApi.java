package org.abaris.weather;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;

public class VWeatherApi extends AbstractVerticle {

    private final int port;
    private final String host;
    private final int applicationPort;

    public VWeatherApi(int applicationPort, int openMeteoPort, String openMeteoHost) {
        this.port = openMeteoPort;
        this.host = openMeteoHost;
        this.applicationPort = applicationPort;
    }

    @Override
    public void start() {
        vertx.createHttpServer()
                .requestHandler(handleWeatherApi())
                .listen(applicationPort);
    }

    private Router handleWeatherApi() {
        Router router = Router.router(vertx);

        router.route("/v1/weather").handler(handleRetrievalCurrentWeatherEndpoint());

        return router;
    }

    private Handler<RoutingContext> handleRetrievalCurrentWeatherEndpoint() {
        return context -> {
            var openMeteoRequest = prepareRequestToOpenMeteo("51.51", "-0.13");

            openMeteoRequest
                    .send()
                    .onSuccess(openMeteoResponse -> {
                        var openMeteoJsonResponse = openMeteoResponse.bodyAsJsonObject();

                        context.response()
                                .putHeader("Content-Type", "application/json")
                                .end(mapToResponseStructure(openMeteoJsonResponse));
                    });
        };
    }

    private static String mapToResponseStructure(JsonObject openMeteoJsonResponse) {
        return new JsonObject()
                .put("latitude", openMeteoJsonResponse.getDouble("latitude"))
                .put("longitude", openMeteoJsonResponse.getDouble("longitude"))
                .put("temperature", openMeteoJsonResponse.getJsonObject("current_weather").getDouble("temperature"))
                .encode();
    }

    private HttpRequest<Buffer> prepareRequestToOpenMeteo(String latitude, String longitude) {
        return WebClient.create(vertx, new WebClientOptions().setTrustAll(true).setSsl(true).setVerifyHost(false))
                .get(port, host, "/v1/forecast")
                .addQueryParam("latitude", latitude)
                .addQueryParam("longitude", longitude)
                .addQueryParam("current_weather", "true");
    }
}
