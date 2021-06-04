package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch1;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    @Test
    public void sample8() throws InterruptedException {
        Observable<Integer> o1 = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(1);
                s.onNext(2);
                s.onNext(3);
            }).start();
        });
        Observable<Integer> o2 = Observable.create(s -> {
            new Thread(() -> {
                s.onNext(4);
                s.onNext(5);
            }).start();
        });
        Observable<Integer> o3 = Observable.merge(o1, o2);
        o3.subscribe(s -> System.out.println("The value " + s));
        Thread.sleep(2000);
    }

    @Test
    public void sample9() {
        Observable<String> o = Observable.create(s -> {
            getDataWithCallback("SOME_KEY", data -> {
                s.onNext(data);
                s.onComplete();
            });
        });
        // lazy, subscribing cause getDataFromServerWithCallback called
        // observable has been reused
        o.subscribe(s -> System.out.println("Subscriber 1: " + s));
        o.subscribe(s -> System.out.println("Subscriber 2: " + s));

        // the power of laziness, composition

        // represents work that can be done, but will only be done if something subscribes to it
        Observable<String> lazyFallback = Observable.just("Fallback");
        o
                .onErrorResumeNext(lazyFallback) // subscribe to if fails
                .subscribe(s -> System.out.println(s));
    }
    private void getDataWithCallback(String key, Consumer<String> consumer) {
        consumer.accept("The data " + Math.random());
    }

    @Test
    public void sample10() throws ExecutionException, InterruptedException {
        // made eager become lazy
        CompletableFuture<String> f1 = getDataasFeature(1);
        CompletableFuture<String> f2 = getDataasFeature(2);

        CompletableFuture<String> f3 = f1.thenCombine(f2, (x, y) -> {
            return x+y;
        });

        String result = f3.get();
        System.out.println(result);
    }
    private CompletableFuture<String> getDataasFeature(int i) {
        return CompletableFuture.completedFuture("The value: " + i);
    }

    @Test
    public void sample11() {
        // Iterable<String> as Stream<String>
        getDataFromLocalMemorySynchronously()
                .skip(10)
                .limit(5)
                .map(s -> s + "_transform")
                .forEach(System.out::println);
    }
    private Stream<String> getDataFromLocalMemorySynchronously() {
        return IntStream
                .range(0, 100)
                .mapToObj(Integer::toString);
    }

    @Test
    public void sample12() {
        // Observable<String>
        getDataFromNetweorkAsynchronously()
                .skip(10)
                .take(5)
                .map(s -> s + "_transform")
                .subscribe(System.out::println);
    }
    private Observable<String> getDataFromNetweorkAsynchronously() {
        return Observable.range(0, 100).map(Objects::toString);
    }

    @Test
    public void sample13() {
        Observable<String> o1 = getDataAsObservable(1);
        Observable<String> o2 = getDataAsObservable(2);

        Observable<String> o3 = Observable.zip(o1, o2, (x, y) -> {
            return x+y;
        });
    }
    private Observable<String> getDataAsObservable(int i) {
        return Observable.just("The value: " + i);
    }

    @Test
    public void sample14() {
        Observable<String> o1 = getDataAsObservable(1);
        Observable<String> o2 = getDataAsObservable(2);

        Observable<String> o3 = Observable.merge(o1, o2);
    }

    @Test
    public void sample15() {
        Flowable aMergeb = getDataA().mergeWith(getDataB());
    }
    public static Single getDataA() {
        return Single.<String>create(o -> {
            o.onSuccess("DataA");
        }).subscribeOn(Schedulers.io());
    }
    public static Single getDataB() {
        return Single.just("Data B:")
                .subscribeOn(Schedulers.io());
    }

    @Test
    public void sample16() {
        Single<String> s1 = getDataasSingle(1);
        Single<String> s2 = getDataasSingle(2);
        Flowable<String> o1 = Single.<String>merge(s1, s2);
    }
    private Single<String> getDataasSingle(int i) {
        return Single.just("Done " + i);
    }

}
