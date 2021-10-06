package io.github.bluething.reactive.techioplayground;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Part06Request {
    ReactiveRepository<User> repository = new ReactiveUserRepository();

    StepVerifier requestAllExpectFour(Flux<User> flux) {
        return StepVerifier.create(flux)
                .expectNextCount(4)
                .expectComplete();
    }

    StepVerifier requestOneExpectSkylerThenRequestOneExpectJesse(Flux<User> flux) {
        return StepVerifier.create(flux)
                .expectNext(User.SKYLER)
                .thenRequest(1)
                .expectNext(User.JESSE)
                .thenCancel();
    }

    Flux<User> fluxWithLog() {
        return repository.findAll().log();
    }

    Flux<User> fluxWithDoOnPrintln() {
        return repository.findAll()
                .doOnSubscribe(s -> System.out.println("Starring:"))
                .doOnNext(u -> System.out.println(u.getFirstname() + " " + u.getLastname()))
                .doOnComplete(() -> System.out.println("The end!"));
    }
}
