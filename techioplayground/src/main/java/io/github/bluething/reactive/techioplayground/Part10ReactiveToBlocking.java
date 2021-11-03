package io.github.bluething.reactive.techioplayground;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Part10ReactiveToBlocking {
    //========================================================================================

    // If we need to block until the value from a Mono is available, use Mono#block() method. It will throw an Exception if the onError event is triggered.
    //Note that we should avoid this by favoring having reactive code end-to-end, as much as possible.
    // We MUST avoid this at all cost in the middle of other reactive code, as this has the potential to lock your whole reactive pipeline.
    User monoToValue(Mono<User> mono) {
        return mono.block();
    }

//========================================================================================

    // We can block for the first or last value in a Flux with blockFirst()/blockLast().
    // We can also transform a Flux to an Iterable with toIterable.
    Iterable<User> fluxToValues(Flux<User> flux) {
        return flux.toIterable();
    }
}
