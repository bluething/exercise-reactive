package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3;

import io.reactivex.Observable;
import org.junit.jupiter.api.Test;

public class SampleCode {

    @Test
    public void filterOperator() {
        Observable<String> strings = Observable.fromArray(new String[]{"Java", "Go", "Erlang", "Elixir"});
        Observable<String> stringWithPrefixE = strings.filter(s -> s.startsWith("E"));

        stringWithPrefixE.subscribe(s -> System.out.println(s));
    }

}
