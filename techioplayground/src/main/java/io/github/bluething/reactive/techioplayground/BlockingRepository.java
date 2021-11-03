package io.github.bluething.reactive.techioplayground;

public interface BlockingRepository<T> {
    void save(T value);

    T findFirst();

    Iterable<T> findAll();

    T findById(String id);
}
