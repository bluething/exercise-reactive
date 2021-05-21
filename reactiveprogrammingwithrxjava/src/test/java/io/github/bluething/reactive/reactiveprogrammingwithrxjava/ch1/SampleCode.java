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

}
