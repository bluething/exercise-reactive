package io.github.bluething.reactive.callback.synchronous;

public class A implements Callback {
    @Override
    public void methodToCallback() {
        System.out.println("Performing callback after synchronous call");
    }
}
