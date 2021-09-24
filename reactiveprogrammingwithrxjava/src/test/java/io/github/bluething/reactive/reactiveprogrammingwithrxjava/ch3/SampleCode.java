package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SampleCode {

    @Test
    public void filterOperator() {
        Observable<String> strings = Observable.fromArray(new String[]{"Java", "Go", "Erlang", "Elixir"});
        Observable<String> stringWithPrefixE = strings.filter(s -> s.startsWith("E"));

        stringWithPrefixE.subscribe(s -> System.out.println(s));
    }

    @Test
    public void mapOperator() {
        Observable<String> formatedInt = Observable.just(8, 9, 10)
                .filter(i -> i % 3 > 0)
                .map(i -> "#" + i*10)
                .filter(s -> s.length() < 4);

        formatedInt.subscribe(s -> System.out.println(s));
    }

    @Test
    public void peekingTheEvent() {
        Observable<String> formatedInt = Observable.just(8, 9, 10)
                .doOnNext(i -> System.out.println("A: " + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B: " + i))
                .map(i -> "#" + i * 10)
                .doOnNext(i -> System.out.println("C: " + i))
                .filter(s -> s.length() < 4)
                .doOnNext(i -> System.out.println("D: " + i));

        formatedInt.subscribe();
    }

    @Test
    public void flatMap() {
        Observable<Integer> ints = Observable.just(1, 2, 3, 4);

        ints.map(i -> i*2)
                .filter(i -> i != 10)
                .subscribe(i -> System.out.println(i));

        ints.flatMap(i -> Observable.just(i*2))
                .flatMap(i -> (i != 10) ? Observable.just(i) : Observable.empty())
                .subscribe(i -> System.out.println(i));
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable() {
        Observable<Customer> customers = Observable.just(new Customer());
        Observable<Order> orders = customers
                .flatMap(customer -> Observable.fromIterable(customer.getOrders()));
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable2() {
        Observable<Customer> customers = Observable.just(new Customer());
        Observable<Order> orders = customers
                .map(Customer::getOrders)
                .flatMap(Observable::fromIterable);
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable3() {
        Observable<Customer> customers = Observable.just(new Customer());
        Observable<Order> orders = customers
                .flatMapIterable(Customer::getOrders);
    }

    private Observable<Long> upload(UUID id) {
        return Observable.just(42L);
    }
    private Observable<Rating> rate(UUID id) {
        return Observable.just(new Rating());
    }

    // naive implementation
    // wait till end to call rate
    private void store(UUID id) {
        upload(id).subscribe(bytes -> {},
                e -> System.out.println(e.getMessage()),
                () -> rate(id)
        );
    }

    @Test
    public void flatMapReactToOtherNotification() {
        UUID id = UUID.randomUUID();
        upload(id)
                .flatMap(
                        bytes -> Observable.empty(),
                        e -> Observable.error(e),
                        () -> rate(id)
                );
    }

}
