package io.github.bluething.reactive.techioplayground;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.concurrent.CompletableFuture;

public class Part09AdaptTest {
    Part09Adapt workshop = new Part09Adapt();
    ReactiveRepository<User> repository = new ReactiveUserRepository();

//========================================================================================

    @Test
    public void adaptToFlowable() {
        Flux<User> flux = repository.findAll();
        Flowable<User> flowable = workshop.fromFluxToFlowable(flux);
        StepVerifier.create(workshop.fromFlowableToFlux(flowable))
                .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
                .verifyComplete();
    }

//========================================================================================

    @Test
    public void adaptToObservable() {
        Flux<User> flux = repository.findAll();
        Observable<User> observable = workshop.fromFluxToObservable(flux);
        StepVerifier.create(workshop.fromObservableToFlux(observable))
                .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
                .verifyComplete();
    }

//========================================================================================

    @Test
    public void adaptToSingle() {
        Mono<User> mono = repository.findFirst();
        Single<User> single = workshop.fromMonoToSingle(mono);
        StepVerifier.create(workshop.fromSingleToMono(single))
                .expectNext(User.SKYLER)
                .verifyComplete();
    }

//========================================================================================

    @Test
    public void adaptToCompletableFuture() {
        Mono<User> mono = repository.findFirst();
        CompletableFuture<User> future = workshop.fromMonoToCompletableFuture(mono);
        StepVerifier.create(workshop.fromCompletableFutureToMono(future))
                .expectNext(User.SKYLER)
                .verifyComplete();
    }
}
