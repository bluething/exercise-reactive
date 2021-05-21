package io.github.bluething.reactive.callback.synchronous;

public class Caller {

    private Callback callback;

    public void register(Callback callback) {
        this.callback = callback;
    }

    public void doSomething() {
        System.out.println("Performing something");

        if (callback != null) {
            callback.methodToCallback();
        }
    }

    public static void main(String[] args) {
        Caller caller = new Caller();
        Callback callback = new A();
        caller.register(callback);
        caller.doSomething();
    }
}
