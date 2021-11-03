package io.github.bluething.reactive.techioplayground;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public class Part10OtherOperations {

    Flux<User> userFluxFromStringFlux(Flux<String> usernameFlux, Flux<String> firstnameFlux, Flux<String> lastnameFlux) {
        return Flux
                .zip(usernameFlux, firstnameFlux, lastnameFlux)
                .map(tuple -> new User(tuple.getT1(), tuple.getT2(), tuple.getT3()));
    }


    Mono<User> useFastestMono(Mono<User> mono1, Mono<User> mono2) {
        return Mono.firstWithSignal(mono1, mono2);
    }


    Flux<User> useFastestFlux(Flux<User> flux1, Flux<User> flux2) {
        return Flux.firstWithSignal(flux1, flux2);
    }


    Mono<Void> fluxCompletion(Flux<User> flux) {
        return flux.then();
    }


    Mono<User> nullAwareUserToMono(User user) {
        return Mono.justOrEmpty(user);
    }


    Mono<User> emptyToSkyler(Mono<User> mono) {
        return mono.defaultIfEmpty(User.SKYLER);
    }


    Mono<List<User>> fluxCollection(Flux<User> flux) {
        return flux.collectList();
    }

}
