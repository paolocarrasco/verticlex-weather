package org.abaris.weather;

import lombok.Value;

import java.util.List;

public record Weather(Double latitude, Double longitude, List<Double> temperatures) {
}
