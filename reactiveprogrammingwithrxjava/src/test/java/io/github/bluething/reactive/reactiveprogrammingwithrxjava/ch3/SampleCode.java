package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

import javax.swing.plaf.TableHeaderUI;
import java.time.DayOfWeek;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3.Sound.DAH;
import static io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3.Sound.DI;
import static io.reactivex.Observable.*;

public class SampleCode {

    @Test
    public void filterOperator() {
        Observable<String> strings = Observable.fromArray(new String[]{"Java", "Go", "Erlang", "Elixir"});
        Observable<String> stringWithPrefixE = strings.filter(s -> s.startsWith("E"));

        stringWithPrefixE.subscribe(s -> System.out.println(s));
    }

    @Test
    public void mapOperator() {
        Observable<String> formatedInt = just(8, 9, 10)
                .filter(i -> i % 3 > 0)
                .map(i -> "#" + i*10)
                .filter(s -> s.length() < 4);

        formatedInt.subscribe(s -> System.out.println(s));
    }

    @Test
    public void peekingTheEvent() {
        Observable<String> formatedInt = just(8, 9, 10)
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
        Observable<Integer> ints = just(1, 2, 3, 4);

        ints.map(i -> i*2)
                .filter(i -> i != 10)
                .subscribe(i -> System.out.println(i));

        ints.flatMap(i -> just(i*2))
                .flatMap(i -> (i != 10) ? just(i) : empty())
                .subscribe(i -> System.out.println(i));
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable() {
        Observable<Customer> customers = just(new Customer());
        Observable<Order> orders = customers
                .flatMap(customer -> Observable.fromIterable(customer.getOrders()));
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable2() {
        Observable<Customer> customers = just(new Customer());
        Observable<Order> orders = customers
                .map(Customer::getOrders)
                .flatMap(Observable::fromIterable);
    }

    @Test
    public void useFlatMapToHandleMethodReturnAnIterable3() {
        Observable<Customer> customers = just(new Customer());
        Observable<Order> orders = customers
                .flatMapIterable(Customer::getOrders);
    }

    private Observable<Long> upload(UUID id) {
        return just(42L);
    }
    private Observable<Rating> rate(UUID id) {
        return just(new Rating());
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
                        bytes -> empty(),
                        e -> Observable.error(e),
                        () -> rate(id)
                );
    }

    private Observable<Sound> toMorseCode(char ch) {
        switch (ch) {
            case 'a':
                return just(DI, DAH);
            case 'b':
                return just(DAH, DI, DI, DI);
            case 'c':
                return just(DAH, DI, DAH, DI);
            case 'd':
                return just(DAH, DI, DI);
            case 'e':
                return just(DI);
            case 'f':
                return just(DI, DI, DAH, DI);
            case 'g':
                return just(DAH, DAH, DI);
            case 'h':
                return just(DI, DI, DI, DI);
            case 'i':
                return just(DI, DI);
            case 'j':
                return just(DI, DAH, DAH, DAH);
            case 'k':
                return just(DAH, DI, DAH);
            case 'l':
                return just(DI, DAH, DI, DI);
            case 'm':
                return just(DAH, DAH);
            case 'n':
                return just(DAH, DI);
            case 'o':
                return just(DAH, DAH, DAH);
            case 'p':
                return just(DI, DAH, DAH, DI);
            case 'q':
                return just(DAH, DAH, DI, DAH);
            case 'r':
                return just(DI, DAH, DI);
            case 's':
                return just(DI, DI, DI);
            case 't':
                return just(DAH);
            case 'u':
                return just(DI, DI, DAH);
            case 'v':
                return just(DI, DI, DI, DAH);
            case 'w':
                return just(DI, DAH, DAH);
            case 'x':
                return just(DAH, DI, DI, DAH);
            case 'y':
                return just(DAH, DI, DAH, DAH);
            case 'z':
                return just(DAH, DAH, DI, DI);
            case '0':
                return just(DAH, DAH, DAH, DAH, DAH);
            case '1':
                return just(DI, DAH, DAH, DAH, DAH);
            case '2':
                return just(DI, DI, DAH, DAH, DAH);
            case '3':
                return just(DI, DI, DI, DAH, DAH);
            case '4':
                return just(DI, DI, DI, DI, DAH);
            case '5':
                return just(DI, DI, DI, DI, DI);
            case '6':
                return just(DAH, DI, DI, DI, DI);
            case '7':
                return just(DAH, DAH, DI, DI, DI);
            case '8':
                return just(DAH, DAH, DAH, DI, DI);
            case '9':
                return just(DAH, DAH, DAH, DAH, DI);
            default:
                return empty();
        }
    }
    @Test
    public void flatMap2() {
        Observable.just('S', 'p', 'a', 'r', 't', 'a')
                .map(Character::toLowerCase)
                .flatMap(this::toMorseCode)
                .subscribe(sound -> System.out.println(sound));
    }

    @Test
    public void delay() throws InterruptedException {
        Observable.just("Lorem", "ipsum", "dolor", "sit", "amet",
                "consectetur", "adipiscing", "elit")
                .delay(word -> timer(word.length(), TimeUnit.SECONDS))
                .subscribe(System.out::println);

        Thread.sleep(15000);
    }

    @Test
    public void replaceDelayWithTimerAndFlatMap() throws InterruptedException {
        Observable.just("Lorem", "ipsum", "dolor", "sit", "amet",
                        "consectetur", "adipiscing", "elit")
                        .flatMap(word -> timer(word.length(), TimeUnit.SECONDS).map(x -> word))
                                .subscribe(System.out::println);

        Thread.sleep(15000);
    }

    @Test
    public void flatMapOrder() throws InterruptedException {
        Observable.just(10L, 1L)
                .flatMap(x -> just(x)
                        .delay(x, TimeUnit.SECONDS))
                .subscribe(System.out::println);

        Thread.sleep(11000);
    }

    private Observable<String> loadRecordsFor(DayOfWeek dow) {
        switch (dow) {
            case SUNDAY:
                return
                        interval(90, TimeUnit.MILLISECONDS)
                                .take(5)
                                .map(i -> "Sun-" + i);
            case MONDAY:
                return
                        interval(65, TimeUnit.MILLISECONDS)
                                .take(5)
                                .map(i -> "Mon-" + i);
            default:
                throw new IllegalArgumentException("Illegal: " + dow);
        }
    }

    @Test
    public void flatMapOrder2() throws InterruptedException {
        Observable.just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .flatMap(this::loadRecordsFor)
                .subscribe(System.out::println);

        Thread.sleep(1000);
    }

    @Test
    public void concatMap() throws InterruptedException {
        Observable.just(DayOfWeek.SUNDAY, DayOfWeek.MONDAY)
                .concatMap(this::loadRecordsFor)
                .subscribe(System.out::println);
        Thread.sleep(1000);
    }
}
