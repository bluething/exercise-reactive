package io.github.bluething.reactive.reactiveprogrammingwithrxjava.ch3;

import java.util.Arrays;
import java.util.List;

public class Customer {
    List<Order> getOrders() {
        return Arrays.asList(new Order(), new Order());
    }
}
