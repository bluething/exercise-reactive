### Introduction

RxJava is a specific implementation of reactive programming for Java and Android that is influenced by functional programming.  
It favors _function composition_, _avoidance of global state and side effects_, and _thinking in streams_ to compose asynchronous and event-based programs.  
It begins with the _observer pattern_ of producer/consumer callbacks and extends it with dozens of operators that allow _composing_, _transforming_, _scheduling_, _throttling_, _error handling_, and _lifecycle management_.

Reactive programming focused on _reacting to changes_, such as data values or events. It can and often is done imperatively. A callback is an approach to reactive programming done imperatively.

Reactive-functional programming is an approach to programming—an abstraction on top of imperative systems—that allows us to program asynchronous and event-driven use cases without having to think like the computer itself and imperatively define the complex interactions of state, particularly across thread and network boundaries.  
Useful when it comes to asynchronous and event-driven systems (because concurrency and parallelism are involved). It's hard to write high-performance, efficient, scalable, and correct concurrent software.

Reactive-functional programming try to solve concurrency and parallelism problems. More colloquially, it is solving callback hell.

When we need reactive programming?  
1. Processing user events.  
2. Responding to and processing any and all latency-bound IO events.  
3. Handling events or data pushed at an application by a producer it cannot control.

If we're handling only one event stream, reactive-_imperative_ programming with a callback is going to be fine.

If your program is like most though, you need to combine events (or asynchronous responses from functions or network calls), have conditional logic interacting between them, and must handle failure scenarios and resource cleanup on any and all of them.  
Then reactive-functional programming is a good choice.

### Observable

1. Represents a stream of data or events.  
2. Push (reactive) but can also be used for pull (interactive).  
3. Lazy.  
4. It can be used asynchronously or synchronously.  
5. It can represent 0, 1, many, or infinite values or events over time.

#### Push versus Pull

Observable and related Observer type signatures support events being pushed at it.  
Observable type also supports an asynchronous feedback channel (also sometimes referred to as async-pull or reactive-pull), as an approach to flow control or backpressure in async systems.

To support receiving events via push, an Observable/Observer pair connect via subscription.  
```java
interface Observable<T> {
    Subscription subscribe(Observer s)
}
```  
```java
interface Observer<T> {
    void onNext(T t)
    void onError(Throwable t)
    void onCompleted()
}
```  
onNext(). Can be never be called, only once, many, or infinite times.  
onError(). Terminal event, called only once.  
onCompleted().Terminal event, called only once.  
When a terminal event is called, the Observable stream is finished and no further events can be sent over it. Terminal events might never occur if the stream is infinite and does not fail.

Advance type of Observer is Subscriber, for interactive pull.  
```java
interface Subscriber<T> implements Observer<T>, Subscription {
    void onNext(T t)
    void onError(Throwable t)
    void onCompleted()
    ...
    void unsubscribe()
    void setProducer(Producer p)
}
```  
unsubscribe(). Allow a subscriber to unsubscribe from an Observable stream.  
setProducer(). Form a bidirectional communication channel between the producer and consumer used for flow control.

#### Async versus Sync

An Observable can be synchronous, and in fact defaults to being synchronous. RxJava never adds concurrency unless it is asked to do so.  
A synchronous Observable would be subscribed to, emit all data using the subscriber’s thread, and complete (if finite). An Observable backed by blocking network I/O would synchronously block the subscribing thread and then emit via onNext() when the blocking network I/O returned.  
See [sample1()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code

Why synchronous? Sometimes it is appropriate to synchronously fetch data from an in-memory cache and return it immediately. [sample1()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code don't need concurrency.

What we need to think is _whether the Observable event production is blocking or nonblocking, not whether it is synchronous or asynchronous_.

The RxJava Observable is purposefully agnostic with regard to async versus sync, and whether concurrency exists or where it comes from. The implementor decide what is best.  
Why this is good? Concurrency can come from multiple places, not just threadpools.

When to use synchronous?  
1. In memory data.  
2. Synchronous computation (operator)

##### In memory data

The key point is constant microsecond/nanosecond lookup times. The Observable can just fetch the data synchronously and emit it on the subscribing thread.  
See [sample2()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code.

If the data not in memory? Perform the network call asynchronously and return the data when it arrives. See [sample3()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code

##### Synchronous computation (operator)