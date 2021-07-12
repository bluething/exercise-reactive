### Observable

![observable](https://github.com/bluething/exercisereactive/blob/main/images/observable.png?raw=true)

An _observer_ _subscribes_ to an _Observable_. An Observable emits items or sends notifications to its observers by calling the observer's methods.

An advantage of this approach is that when we have a bunch of tasks that are not dependent on each other, we can start them all at the same time rather than waiting for each one to finish before starting the next one — that way, our entire bundle of tasks only takes as long to complete as the longest task in the bundle.

#### What is observable?

1. Represents a stream of data or events.
2. Push (reactive) but can also be used for pull (interactive).
3. Lazy.
4. It can be used asynchronously (default) or synchronously.
5. It can represent 0, 1, many, or infinite values or events over time.

#### Synchronous vs Asynchronous process

Synchronous  
1. Call a method.  
2. Store the return value from that method in a variable.  
3. Use that variable and its new value to do something useful.

Asynchronous  
1. Define a method that does something useful with the return value from the asynchronous call; this method is part of the observer.  
2. Define the asynchronous call itself as an Observable.  
3. Attach the observer to that Observable by subscribing it (this also initiates the actions of the Observable).  
4. Go on with your business; whenever the call returns, the observer’s method will begin to operate on its return value or values — the items emitted by the Observable.

What we need to think is whether the Observable event production is blocking or nonblocking, not whether it is synchronous or asynchronous.

Why this design be useful?  
1. Concurrency can come from multiple places, not just threadpools.  
2. There are reason to use synchronous.  
    - Access in memory data (constant look up time)  
    - Synchronous computation (operator), performance reason.

#### Concurrency and Parallelism

We can achieve concurrency and parallelism operation via _composition_ of async Observables. The contract of an RxJava Observable is that events (`onNext()`, `onCompleted()`, `onError()`) can never be emitted concurrently.

#### The Observable Contract

##### An Observable communicates with its observers with the following notifications

1. OnNext. Zero or more items that is emitted by the Observable to the observer. Following by either an OnCompleted or an OnError notification, but not both.  
2. OnCompleted. Indicates that the Observable has completed successfully and that it will be emitting no further items. Terminal operation.  
3. OnError. Indicates that the Observable has terminated _with a specified error condition_ and that it will be emitting no further items. Terminal operation.  
4. OnSubscribe (optional). Indicates that the Observable is ready to accept Request notifications from the observer.

It is proper for an Observable to issue no notifications, to issue only an OnCompleted or an OnError notification, or to issue only OnNext notifications.

Observables must issue notifications to observers _serially_ (not in parallel). They may issue these notifications from different threads, but there must be a formal happens-before relationship between the notifications.

##### An observer communicates with its Observable by means of the following notifications

1. Subscribe. Indicates that the observer is ready to receive notifications from the Observable.  
2. Unsubscribe. Indicates that the observer no longer wants to receive notifications from the Observable.  
3. Request (optional). Indicates that the observer wants no more than a particular number of additional OnNext notifications from the Observable.

When an Observable issues an OnError or OnComplete notification to its observers, this ends the subscription. Observers do not need to issue an Unsubscribe notification to end subscriptions that are ended by the Observable in this way.

If a second observer subscribes to an Observable that is already emitting items to a first observer, it is up to the Observable whether it will thenceforth emit the same items to each observer, or whether it will replay the complete sequence of items from the beginning to the second observer, or whether it will emit a wholly different sequence of items to the second observer. There is no general guarantee that two observers of the same Observable will see the same sequence of items.

##### Backpressure

Not all Observables or operators honor backpressure. An Observable may implement backpressure if it detects that its observer implements Request notifications and understands OnSubscribe notifications.  
If an Observable implements backpressure and its observer employs backpressure, the Observable will not begin to emit items to the observer immediately upon subscription. Instead, it will issue an OnSubscribe notification to the observer.

#### Reference

[Observable](http://reactivex.io/documentation/observable.html)  
[The Observable Contract](http://reactivex.io/documentation/contract.html)