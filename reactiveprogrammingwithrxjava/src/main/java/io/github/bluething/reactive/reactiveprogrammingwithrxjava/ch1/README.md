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
`onNext()`. Can be never be called, only once, many, or infinite times.  
`onError()`. Terminal event, called only once.  
`onCompleted()`.Terminal event, called only once.  
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
`unsubscribe()`. Allow a subscriber to unsubscribe from an Observable stream.  
`setProducer()`. Form a bidirectional communication channel between the producer and consumer used for flow control.

#### Async versus Sync

An Observable can be synchronous, and in fact _defaults to being synchronous_. RxJava never adds concurrency unless it is asked to do so.  
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

Operator use synchronous computation because of performance reason.

See [sample4()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code.  
If the map operator asynchronous each number scheduled onto a thread where the string concatenation would be performed.  
It's have nondeterministic latency due to scheduling, context switching, and so on

Most Observable function pipelines are synchronous (unless a specific operator needs to be async, such as timeout or observeOn), whereas the Observable itself can be async.

See [sample5()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code.  
The observable is asynchronous (use different thread with subscriber to emit the value), subscriber non blocking. Filter and map operator are synchronously executed on the calling thread that emits the events.

#### Concurrency and Parallelism

Parallelism is _simultaneous_ execution of tasks, typically on different CPUs or machines.  
Concurrency, on the other hand, is the composition or _interleaving_ of multiple tasks.  
Being multithreaded is concurrency, but parallelism only occurs if those threads are being scheduled and executed on different CPUs at the exact same time.

Individual Observable streams permit neither concurrency nor parallelism (always be serialized and thread-safe). Instead, they are achieved via composition of async Observables.  
onNext(), onCompleted(), onError() can never be emitted concurrently. Each event can be emitted from a different thread, as long as the emissions are not concurrent.

See [sample6()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code how to use sequential concurrent. It's better to use schedulers instead of thread.  
See [sample7()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code how we try to use multiple thread that can invoke onNext() concurrently. This violate the [contract](https://github.com/bluething/exercisereactive/tree/main/observable#the-observable-contract).

The solution? Composition, using merge or flatMap operator.

See [sample8()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code we use merge.  
Because we use asynchronous:  
1. 1 will appear before 2.  
2. 4 will appear before 5.  
3. The order between one/two and three/four is unspecified.

Why onNext prohibited from being called simultaneously?  
1. onNext() is meant for us humans to use. If onNext() could be invoked concurrently, it would mean that every Observer would need to code defensively for concurrent invocation, even when not expected or wanted.  
2. Some operations just aren't possible with concurrent emission, for example scan and reduce.  
3. Performance is affected by synchronization overhead because all observers and operators would need to be thread-safe.  
4. It is often slower to do generic fine-grained parallelism.

#### Lazy versus Eager

The Observable type is lazy, meaning it _does nothing until it is subscribed to_. This differs from an eager type such as a Future, which when created _represents active work_.  
Subscription, not construction starts work. Subscribing to the Observable causes the work to be done  
Observables can be reused. Future can't be reused.

See [sample9()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java)

#### Duality

A Rx Observable is the async "dual" of an Iterable. Anything you can do synchronously via pull with an Iterable and Iterator can be done asynchronously via push with an Observable and Observer. This means that the same programming model can be applied to both!

| Pull (Iterable) | Push (Observerable) |
| --- | ---- |
| T next() | onNext(T)|
| throws Exception | onError(Throwable) |
| returns | onCompleted() |

See [sample11()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code for Iterable.  
See [sample12()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code for Obervable.

#### Cardinality

|| One | Many |
| --- | --- | ---- |
| Synchronous | T getData() | Iterable<T> getData()|
| Asynchronous | Future<T> getData() | Observable<T> getData() |

Why might an Observable be valuable instead of just Future?  
The most obvious reason is that we are dealing with either an event stream or a multivalued response.  
The less obvious reason is composition of multiple single-valued responses.

##### Event Stream

Over time the producer pushes events at the consumer.    
```java
// producer
Observable<Event> mouseEvents = ...;

// consumer
mouseEvents.subscribe(e -> doSomethingWithEvent(e));
```

If using Future  
```java
// producer
Future<Event> mouseEvents = ...;

// consumer
mouseEvents.onSuccess(e -> doSomethingWithEvent(e));
```  
The onSuccess callback could have received the "last event," but some questions remain:  
1. Does the consumer now need to poll?  
2. Will the producer enqueue them?  
3. Or will they be lost in between each fetch?
   
In the absence of Observable, a callback approach would be better than modeling this with a Future.

##### Multiple value

Anywhere that a _List_, _Iterable_, or _Stream_ would be used, Observable can be used instead.  
```java
// producer
Observable<Friend> friends = ...

// consumer
friends.subscribe(friend -> sayHello(friend));
```  
If using Future  
```java
// producer
Future<List<Friend>> friends = ...

// consumer
friends.onSuccess(listOfFriends -> {
   listOfFriends.forEach(friend -> sayHello(friend));
});
```  
Why use the Observable<Friend> approach? Performance or latency benefit if the data is big, or the remote data source must fetch different portions of the list from different locations.

What kind of latency benefit? Using Observable we don't need to wait for the entire collection. The consumer receives them immediately and "time to first item" can be significantly lower than the last and slowest item.  
As a note, ordering of the stream must be sacrificed. If order is eventually important to the consumer, put in data or metadata and the client can then sort or position the items as needed.

Additionally, it keeps memory usage limited to that needed per item rather than needing to allocate and collect memory for the entire collection.

##### Composition

A multivalued Observable type is also useful when composing single-valued responses, such as from Futures.

When merging together multiple Futures, they emit another Future with a single value.  
See [sample10()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code for Iterable.  
We are waiting until all Futures are completed before emitting anything.

See [sample13()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code for Obervable.

It is preferable to emit each returned Future value as it completes, we can use merge.  
It allows composing the results (even if each is just an Observable emitting one value) into a stream of values that are each emitted as soon as they are ready.  
See [sample14()](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch1/SampleCode.java) code.

##### Single

Is a lazy equivalent to a Future. Think of it as a Future with two benefits:  
1. It is lazy, so it can be subscribed to multiple times and easily composed  
2. It fits the RxJava API, so it can easily interact with an Observable.
   
Basic request/response is a kind of single value

Behavior of Single:  
1. It can respond with an error.  
2. Never respond.  
3. Respond with a success.

Behavior of Observerable:  
1. It can respond with an error.  
2. Never respond.  
3. Respond with a success.  
4. Respond successfully with no data and terminate.  
5. Respond successfully with a single value and terminate.  
6. Respond successfully with multiple values and terminate.  
7. Respond successfully with one or more values and never terminate (waiting for more data).

##### Completable

Represent successful or failed completion. This use case is common when doing asynchronous writes for which no return value is expected but notification of successful or failed completion is needed.

##### Zero to Infinity

Single is an "Observable of One," and Completable is an "Observable of None."

| |Zero| One | Many |
| --- |--- | --- | ---- |
|Synchronous| void doSomething() | T getData() | Iterable<T> getData() |
|Asynchronous| Completable doSomething() | Single<T> getData() | Observable<T> getData()|

##### Blocking versus Nonblocking I/O

The author of this book doing performance comparison between blocking and nonblocking I/O with Tomcat and Netty on Linux.

What they learn:  
1. Netty code is more efficient than Tomcat code, allowing it to consume less CPU per request.  
2. The Netty event-loop architecture reduces thread migrations under load, which improves CPU cache warmth and memory locality, which improves CPU Instructions-per-Cycle (IPC), which lowers CPU cycle consumption per request.  
3. Tomcat code has higher latencies under load due to its thread pool architecture, which involves thread pool locks (and lock contention) and thread migrations to service load.

### Additional reading:

[Concurrency vs Event Loop](https://medium.com/the-legend/concurrency-vs-event-loop-5648882ad668)  
[Netty event loop](https://learning.oreilly.com/library/view/netty-in-action/9781617291470/kindle_split_016.html)  
[RxNetty vs Tomcat performance result](https://github.com/Netflix-Skunkworks/WSPerfLab/blob/master/test-results/RxNetty_vs_Tomcat_April2015.pdf)  
[RxNetty vs Tomcat code](https://github.com/Netflix-Skunkworks/WSPerfLab/tree/master/ws-impls)  
[Applying Reactive Programming with RxJava at GOTO Chicago 2015 ](https://speakerdeck.com/benjchristensen/applying-reactive-programming-with-rxjava-at-goto-chicago-2015?slide=146)