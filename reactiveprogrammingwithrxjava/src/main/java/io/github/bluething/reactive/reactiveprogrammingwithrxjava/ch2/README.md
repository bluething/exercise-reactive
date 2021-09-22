### Reactive Extension

`rx.Observable<T>` represent a flowing sequence of values, think as a stream of events.

Similarities between `Observable<T>` and `Iterable<T`>:  
- Can have zero to an infinite number of values of type `T`  
- Can signal its client that it has no more items to produce

But `Observable<T>`  
- Is push-based, can become pull-based with backpressure.  
- Can produce an arbitrary number of events, does not need to be backed by the underlying collection.

Obervable types

| Type | Description | 
| --- | --- |
| Flowable<T> | Emits 0 or n items and terminates with an success or an error event. Supports backpressure, which allows to control how fast a source emits items. |
| Observable<T> | Emits 0 or n items and terminates with an success or an error event. |
| Single<T> | Emits either a single item or an error event. The reactive version of a method call. |
| Maybe<T> | Succeeds with an item, or no item, or errors. The reactive version of an Optional. |
| Completable | Either completes with an success or with an error event. It never emits items. The reactive version of a Runnable. |

`Observable<T>` can actually produce three types of events:  
1. Values of type T, as declared by Observable  
2. Error event.  
3. Completion event.

Every `Observable` can emit an arbitrary number of values optionally followed by completion or error (but not both).  
An instance of `Observable` does not emit any events until someone is actually interested in receiving them.  
The RxJava contract makes sure that our callback will not be invoked from more than one thread at a time, even though events can be emitted from many threads.

How to control the listener? Using `Subscription` and `Subscriber<T>`  
For example we subscribed for stock price changes, but when the price falls below $1, we no longer want to listen.  
For RxJava2 use Disposable, see [here](https://www.rallyhealth.com/coding/migrating-to-rxjava-2) and [here](https://www.vogella.com/tutorials/RxJava/article.html)  

[2 ways](https://github.com/bluething/exercisereactive/blob/main/reactiveprogrammingwithrxjava/src/test/java/io/github/bluething/reactive/reactiveprogrammingwithrxjava/ch2/SampleCode.java) to do this:  
1. From Disposable  
2. Inside DisposableObserver

Why we need to unsubscribe?  
Avoids memory leaks and unnecessary load on the system. However, there are cases in which subscribers come and go while the Observable keeps producing events forever.

How to create Observable?

| Function | Description |
| --- | --- |
| Observable.just(value) | Creates an Observable instance that emits exactly one value to all future subscribers and completes afterward. |
| Observable.from(values) | It converts an Iterable, a Callable, or an Array into an Observable. `fromIterable(@NonNull Iterable<? extends T> source)`,  `fromArray(T... items)`, `fromCallable(@NonNull Callable<? extends T> callable), fromArray(T... items)`|
| Observable.range(from, n) | Produces n integer numbers starting from from. |
| Observable.empty() | Completes immediately after subscription, without emitting any values. |
| Observable.never() | Such Observable never emits any notifications, neither values nor completion or error. This stream is useful for testing purposes. |
| Observable.error() | Emits an onError() notification immediately to every subscriber. |
| Observable.timer() | creates an Observable that emits one particular item after a span of time that we specify.|
| Observable.interval() | Returns an Observable that emits an infinite sequence of ascending integers, with a constant interval of time of your choosing between emissions.|
| Observable.defer() | Does not create the Observable until the observer subscribes, and create a fresh Observable for each observer.|

What if we have multiple subscriber?  
Use `cache()`, example use case is observable that doing database query or heavyweight computation.  
What `cache()` does is stand between subscribe() and our custom Observable, it keeps a copy of all notifications internally.  
`cache()` plus infinite stream is the recipe for a disaster, also known as OutOfMemoryError.