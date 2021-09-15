package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

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
