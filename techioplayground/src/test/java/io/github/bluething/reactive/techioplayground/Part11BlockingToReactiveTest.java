package io.github.bluething.reactive.techioplayground;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Iterator;

import static org.assertj.core.api.Assertions.assertThat;

public class Part11BlockingToReactiveTest {
    Part11BlockingToReactive workshop = new Part11BlockingToReactive();

//========================================================================================

    @Test
    public void slowPublisherFastSubscriber() {
        BlockingUserRepository repository = new BlockingUserRepository();
        Flux<User> flux = workshop.blockingRepositoryToFlux(repository);
        assertThat(repository.getCallCount()).isEqualTo(0).withFailMessage("The call to findAll must be deferred until the flux is subscribed");
        StepVerifier.create(flux)
                .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
                .verifyComplete();
    }

//========================================================================================

    @Test
    public void fastPublisherSlowSubscriber() {
        ReactiveRepository<User> reactiveRepository = new ReactiveUserRepository();
        BlockingUserRepository blockingRepository = new BlockingUserRepository(new User[]{});
        Mono<Void> complete = workshop.fluxToBlockingRepository(reactiveRepository.findAll(), blockingRepository);
        assertThat(blockingRepository.getCallCount()).isEqualTo(0);
        StepVerifier.create(complete)
                .verifyComplete();
        Iterator<User> it = blockingRepository.findAll().iterator();
        assertThat(it.next()).isEqualTo(User.SKYLER);
        assertThat(it.next()).isEqualTo(User.JESSE);
        assertThat(it.next()).isEqualTo(User.WALTER);
        assertThat(it.next()).isEqualTo(User.SAUL);
        assertThat(it.hasNext()).isFalse();
    }
}
