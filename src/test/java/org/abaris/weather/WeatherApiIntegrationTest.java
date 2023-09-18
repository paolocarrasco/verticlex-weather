package org.abaris.weather;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.junit5.RunTestOnContext;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Random;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest(httpsEnabled = true)
@ExtendWith(RunTestOnContext.class)
@ExtendWith(VertxExtension.class)
class WeatherApiIntegrationTest {

    // random value to avoid port conflicts
    private final int applicationPort = new Random().nextInt(1_000) + 10_000;

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext, WireMockRuntimeInfo wireMockRuntimeInfo) {
        vertx.deployVerticle(new VWeatherApi(applicationPort, wireMockRuntimeInfo.getHttpsPort(), "localhost")).onComplete(testContext.succeedingThenComplete());
    }

    @Test
    void givenCityIsValidWhenWeatherEndpointIsHitThenItReturnsCurrentTemperature(Vertx vertx, VertxTestContext testContext) {
        vertx.createHttpClient().request(HttpMethod.GET, applicationPort, "localhost", "/v1/weather?city=London&country=UK")
                .onSuccess(request -> request.send()
                        .onSuccess(response -> response.bodyHandler(body -> {
                            var weatherForLondon = body.toJsonObject().mapTo(Weather.class);

                            assertThat(weatherForLondon.latitude()).isEqualTo(51.55);
                            assertThat(weatherForLondon.longitude()).isEqualTo(-0.13);
                            assertThat(weatherForLondon.temperature()).isEqualTo(19.3);

                            testContext.completeNow();
                        })));
    }
}
