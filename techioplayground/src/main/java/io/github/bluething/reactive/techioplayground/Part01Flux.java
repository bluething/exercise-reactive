package io.github.bluething.reactive.techioplayground;

import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.TemporalUnit;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Part01Flux {

    public Flux<String> emptyFlux() {
        return Flux.empty();
    }

    public Flux<String> fooBarFluxFromValues() {
        return Flux.just("foo", "bar");
    }

    public Flux<String> fooBarFluxFromList() {
        return Flux.fromIterable(Arrays.asList("foo", "bar"));
    }

    public Flux<String> errorFlux() {
        return Flux.error(new IllegalStateException());
    }

    public Flux<Long> counter() {
        return Flux.interval(Duration.ofMillis(100)).take(10);
    }
}
