package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch2;

import io.reactivex.Observable;
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
