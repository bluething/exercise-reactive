package io.github.bluething.reactive.callback.asynchronous;

public class Caller {

    private Callback callback;

    public void register(Callback callback) {
        this.callback = callback;
    }

    public void doSomething() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Performing something in asynchronous mode");

                if (callback != null) {
                    callback.methodToCallback();
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        Caller caller = new Caller();
        Callback callback = new A();
        caller.register(callback);
        caller.doSomething();
    }

}
