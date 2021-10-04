package io.github.bluething.reactive.techioplayground;

import org.junit.jupiter.api.Assertions;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.function.Supplier;

public class Part03StepVerifier {
    void expectFooBarComplete(Flux<String> flux) {
        StepVerifier.create(flux)
                .expectNext("foo", "bar")
                .verifyComplete();
    }

    void expectFooBarError(Flux<String> flux) {
        StepVerifier.create(flux)
                .expectNext("foo", "bar")
                .verifyError(RuntimeException.class);
    }

    void expectSkylerJesseComplete(Flux<User> flux) {
        StepVerifier.create(flux)
                .expectNextMatches(user -> user.getUsername().equals("swhite"))
                .assertNext(user -> Assertions.assertEquals("jpinkman", user.getUsername()))
                .verifyComplete();
    }

    void expect10Elements(Flux<Long> flux) {
        StepVerifier.create(flux)
                .expectNextCount(10)
                .verifyComplete();
    }

    void expect3600Elements(Supplier<Flux<Long>> supplier) {
        StepVerifier.withVirtualTime(supplier)
                .thenAwait(Duration.ofHours(1))
                .expectNextCount(3600)
                .verifyComplete();
    }


    private void fail() {
        throw new AssertionError("workshop not implemented");
    }
}
