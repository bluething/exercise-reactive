package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch2;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import org.junit.jupiter.api.Test;

public class SampleCode {
    @Test
    public void observableSubscribe1() {
        Observable<Tweet> tweets = Observable.just(new Tweet("This is tweet"));

        tweets.subscribe((Tweet tweet) -> System.out.println(tweet.getText()));
    }

    @Test
    public void observableSubscribe2() {
        Observable<Tweet> tweets = Observable.error(new Exception("Tweet exception"));

        tweets.subscribe(
                (Tweet tweet) -> System.out.println(tweet.getText()),
                (Throwable t) -> System.out.println(t.getMessage())
        );
    }

    @Test
    public void observableSubscribe3() {
        Observable<Tweet> tweets = Observable.just(new Tweet("This is tweet"));

        tweets.subscribe(
                (Tweet tweet) -> System.out.println(tweet.getText()),
                (Throwable t) -> System.out.println(t.getMessage()),
                () -> System.out.println("Completed")
        );
    }

    @Test
    public void observableSubscribe4() {
        Observable<Tweet> tweets = Observable.just(new Tweet("This is tweet"));

        Observer<Tweet> observer = new Observer<Tweet>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Tweet tweet) {
                System.out.println(tweet.getText());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        };

        tweets.subscribe(observer);
    }

    @Test
    public void controllingListener1() {
        Observable<Tweet> tweets = Observable.just(new Tweet("This is tweet"));
        Disposable disposable = tweets.subscribe(System.out::println);

        disposable.dispose();
    }

    @Test
    public void controllingListener2() {
        Observable<Tweet> tweets = Observable.just(new Tweet("This is tweet about RxJava"));
        DisposableObserver<Tweet> tweetDisposableObserver = tweets.subscribeWith(new DisposableObserver<Tweet>() {
            @Override
            public void onNext(@NonNull Tweet tweet) {
                if (tweet.getText().contains("Java")) {
                    System.out.println(tweet.getText());
                    dispose();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("Completed");
            }
        });

        tweetDisposableObserver.dispose();
    }

    //TODO https://howtoprogram.xyz/2017/02/07/how-to-create-observable-in-rxjava-2/

    private static void log(Object message) {
        System.out.println(Thread.currentThread().getName() + ": " + message);
    }

    @Test
    public void blockingChildThread() {
        log("Before");

        Observable.range(5, 3)
                .subscribe(i -> log(i));

        log("After");
    }

    @Test
    public void observableCreateNotEmitEventUntilWeSubscribeIt() {
        Observable<Integer> ints = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                log("Create");
                emitter.onNext(5);
                emitter.onNext(6);
                emitter.onNext(7);
                emitter.onComplete();
                log("Completed");
            }
        });

        log("Starting");
        ints.subscribe(i -> log("Element: " + i));
        log("Exit");
    }

    @Test
    public void observableWithMultipleSubscriber() {
        Observable<Integer> ints = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                log("Create");
                emitter.onNext(42);
                emitter.onComplete();
            }
        }).cache();

        log("Starting");
        ints.subscribe(i -> log("Element: " + i));
        ints.subscribe(i -> log("Element: " + i));
        log("Exit");
    }

    class Tweet {
        private final String text;

        Tweet(String text) {
            this.text = text;
        }

        String getText() {
            return text;
        }
    }
}
