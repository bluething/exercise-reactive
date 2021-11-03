package io.github.bluething.reactive.techioplayground;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Part09Adapt {
    Flowable<User> fromFluxToFlowable(Flux<User> flux) {
        return Flowable.fromPublisher(flux);
    }

    Flux<User> fromFlowableToFlux(Flowable<User> flowable) {
        return Flux.from(flowable);
    }


    Observable<User> fromFluxToObservable(Flux<User> flux) {
        return Observable.fromPublisher(flux);
    }

    Flux<User> fromObservableToFlux(Observable<User> observable) {
        return Flux.from(observable.toFlowable(BackpressureStrategy.BUFFER));
    }


    Single<User> fromMonoToSingle(Mono<User> mono) {
        return Single.fromPublisher(mono);
    }

    Mono<User> fromSingleToMono(Single<User> single) {
        return Mono.from(single.toFlowable());
    }


    CompletableFuture<User> fromMonoToCompletableFuture(Mono<User> mono) {
        return mono.toFuture();
    }

    Mono<User> fromCompletableFutureToMono(CompletableFuture<User> future) {
        return Mono.fromFuture(future);
    }
}
