package io.github.bluething.reactive.callback.asynchronous;

public class A implements Callback {
    @Override
    public void methodToCallback() {
        System.out.println("Performing callback after asynchronous call");
    }
}
