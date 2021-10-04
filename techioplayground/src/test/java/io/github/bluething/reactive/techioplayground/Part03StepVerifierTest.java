package io.github.bluething.reactive.techioplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class Part03StepVerifierTest {
    Part03StepVerifier workshop = new Part03StepVerifier();

//========================================================================================

    @Test
    public void expectElementsThenComplete() {
        workshop.expectFooBarComplete(Flux.just("foo", "bar"));
    }

//========================================================================================

    @Test
    public void expect2ElementsThenError() {
        workshop.expectFooBarError(Flux.just("foo", "bar").concatWith(Mono.error(new RuntimeException())));
    }

//========================================================================================

    @Test
    public void expectElementsWithThenComplete() {
        workshop.expectSkylerJesseComplete(Flux.just(new User("swhite", null, null), new User("jpinkman", null, null)));
    }

//========================================================================================

    @Test
    public void count() {
        workshop.expect10Elements(Flux.interval(Duration.ofSeconds(1)).take(10));
    }

//========================================================================================

    @Test
    public void countWithVirtualTime() {
        workshop.expect3600Elements(() -> Flux.interval(Duration.ofSeconds(1)).take(3600));
    }

}