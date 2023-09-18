package org.abaris.weather;

import io.vertx.core.Vertx;

public class MainApp {
    public static void main(String[] args) {
        Vertx.vertx().deployVerticle(new VWeatherApi(8080, 443, "api.open-meteo.com"));
    }
}
