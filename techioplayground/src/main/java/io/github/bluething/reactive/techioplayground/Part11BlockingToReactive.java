package io.github.bluething.reactive.techioplayground;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class Part11BlockingToReactive {
    //========================================================================================

    // The subscribeOn method allow to isolate a sequence from the start on a provided Scheduler.
    // The Schedulers.elastic() will create a pool of threads that grows on demand, releasing threads that haven't been used in a while automatically.
    // We will need to wrap the call to the repository inside a Flux.defer lambda.
    Flux<User> blockingRepositoryToFlux(BlockingRepository<User> repository) {
        return Flux.defer(() -> Flux.fromIterable(repository.findAll()))
                .subscribeOn(Schedulers.boundedElastic());
    }

//========================================================================================

    // For slow subscribers (eg. saving to a database), we can isolate a smaller section of the sequence with the publishOn operator.
    // Unlike subscribeOn, it only affects the part of the chain below it, switching it to a new Scheduler.
    // you can use doOnNext to perform a save on the repository, but first use the trick above to isolate that save into its own execution context.
    // You can make it more explicit that you're only interested in knowing if the save succeeded or failed by chaining the then() operator at the end, which returns a Mono<Void>.
    Mono<Void> fluxToBlockingRepository(Flux<User> flux, BlockingRepository<User> repository) {
        return flux.publishOn(Schedulers.boundedElastic())
                .doOnNext(repository::save)
                .then();
    }

}
