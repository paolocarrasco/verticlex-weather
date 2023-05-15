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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@WireMockTest(httpPort = 8081)
@ExtendWith(RunTestOnContext.class)
@ExtendWith(VertxExtension.class)
class WeatherApiIntegrationTest {

    @BeforeEach
    void setUp(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new VWeatherApi(8081, "localhost")).onComplete(testContext.succeedingThenComplete());
    }

    @Test
    @SuppressWarnings("JUnitMalformedDeclaration")
    void testWeatherApi(Vertx vertx, VertxTestContext testContext, WireMockRuntimeInfo wireMockRuntimeInfo) {
        wireMockRuntimeInfo.getHttpPort();
        vertx.createHttpClient().request(HttpMethod.GET, 8080, "localhost", "/weather?city=London&country=uk&units=metric&days=7")
                .onSuccess(request -> request.send()
                        .onSuccess(response -> response.bodyHandler(body -> {
                            var weatherForLondon = body.toJsonObject().mapTo(Weather.class);

                            assertThat(weatherForLondon.latitude()).isEqualTo(51.55);
                            assertThat(weatherForLondon.longitude()).isEqualTo(-0.13);

                            assertThat(weatherForLondon.temperatures()).asList().hasSize(24)
                                    .containsExactly(
                                            11.0,
                                            11.1,
                                            11.0,
                                            10.9,
                                            10.7,
                                            10.7,
                                            10.4,
                                            10.5,
                                            10.8,
                                            10.8,
                                            11.2,
                                            11.5,
                                            14.2,
                                            14.3,
                                            14.5,
                                            15.4,
                                            14.6,
                                            13.5,
                                            13.0,
                                            11.7,
                                            10.3,
                                            9.4,
                                            8.8,
                                            8.2
                                    );
                            testContext.completeNow();
                        })));
    }
}
