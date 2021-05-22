package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch1;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SampleCode {

    private Map<String, String> caches = new ConcurrentHashMap<>();

    private void putInCaches(String key, String value) {
        caches.put(key, value);
    }
    private String getFromCaches(String key) {
        return caches.get(key);
    }

    @Test
    public void sample1() {
        Observable.create(s -> {
            s.onNext("Hello World!");
            s.onComplete();
        }).subscribe(hello -> System.out.println(hello));
    }

    @Test
    public void sample2() {
        putInCaches("SOMEKEY", "Hello World!!");
        Observable.create(s -> {
            s.onNext(caches.get("SOMEKEY"));
            s.onComplete();
        }).subscribe(value -> System.out.println(value));
    }

    @Test
    public void sample3() throws InterruptedException {
        Observable.create(s -> {
            String fromCaches = getFromCaches("SOMEKEY");
            if (fromCaches != null) {
                // emit synchronously
                s.onNext(fromCaches);
                s.onComplete();
            } else {
                getDataAsynchronously("SOMEKEY").onResponse(v -> {
                    putInCaches("SOMEKEY", "Hello World!!");
                    s.onNext(v);
                    s.onComplete();
                }).onError(exception -> {
                    s.onError(exception);
                });
            }
        }).subscribe(s -> System.out.println(s));
        Thread.sleep(2000);
    }
    private Callback getDataAsynchronously(String key) {
        final Callback callback = new Callback();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                callback.getOnResponse().accept("Hello World!!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        return callback;
    }

    @Test
    public void sample4() {
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
        });

        o.map(i -> "Number " + i).subscribe(s -> System.out.println(s));
    }

    @Test
    public void sample5() throws InterruptedException {
        Observable.<Integer>create(s -> {
            new Thread(() -> s.onNext(2), "My thread").start();
        })
                .doOnNext(i -> System.out.println(Thread.currentThread()))
                .filter(i -> i % 2 ==0)
                .map(i -> "Value " + i + " processed on " + Thread.currentThread())
                .subscribe(s -> System.out.println("The value " + s)
                );
        System.out.println("This statement print before values are emitted");
        Thread.sleep(1000);
    }

    @Test
    public void sample6() throws InterruptedException {
        Observable.<Integer>create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
            }).start();
        }).subscribe(s -> System.out.println("The value " + s));
        Thread.sleep(2000);
    }

    @Test
    public void sample7() throws InterruptedException {
        Observable.<Integer>create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
            }).start();
            new Thread(() -> {
                s.onNext(4);
                s.onNext(5);
            }).start();
            // need to safely wait for both threads to complete to call onComplete()
        }).subscribe(s -> System.out.println("The value " + s));
        Thread.sleep(2000);
    }
}
