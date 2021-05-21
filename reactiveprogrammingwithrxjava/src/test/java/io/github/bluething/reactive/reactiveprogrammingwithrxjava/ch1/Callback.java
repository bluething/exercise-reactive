package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch1;

import java.util.function.Consumer;

public class Callback {

    private Consumer<String> onResponse = x -> {};
    private Consumer<Exception> onError = x -> {};

    Callback onResponse(Consumer<String> consumer) {
        this.onResponse = consumer;
        return this;
    }

    Callback onError(Consumer<Exception> consumer) {
        this.onError = consumer;
        return this;
    }

    public Consumer<String> getOnResponse() {
        return onResponse;
    }

    public Consumer<Exception> getOnError() {
        return onError;
    }
}
