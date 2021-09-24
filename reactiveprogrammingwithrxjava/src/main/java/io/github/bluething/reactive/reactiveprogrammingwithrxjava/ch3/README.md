### Operators and Transformations

An operator is a function that takes upstream Observable<T> and returns downstream Observable<R>, where types T and R might or might not be the same. Operators allow composing simple transformations into complex processing graphs.  
What happens to the original upstream strings source? Every single operator returns a _new_ Observable, leaving the original one untouched.

Filter  
![filter](https://github.com/bluething/exercisereactive/blob/main/images/filterdiagram.png?raw=true)

Map 1 to 1 transformation  
![map](https://github.com/bluething/exercisereactive/blob/main/images/mapdiagram.png?raw=true)

Observables are lazy, not a single operator like `map()` or `filter()` is evaluated until someone actually shows an interest.  
Every time we use any operator, we basically create a wrapper around original Observable. This wrapper can intercept events flying through it but typically does not subscribe on its own.  
Using `doOnNext()` that allows looking at items going through without touching them.  
Every line in new Observable, in a way wrapping the original one.
As a rule of thumb, all types wrapped with Observable should be immutable for all practical applications.

What happens when Rx sees subscribe() at the very end of the chain? It will walk backward from bottom to top until find source of events.

flatMap  
![flat map](https://github.com/bluething/exercisereactive/blob/main/images/flatmapdiagrampng?raw=true)  
`flatMap()` takes `Observable<T>` and a function from `T` to `Observable<R>`.  
`flatMap()` first constructs `Observable<Observable<R>>` replacing all upstream values of type `T` with `Observable<R>` (just like map()).  
Then it automatically subscribes to these inner `Observable<R>` streams to produce a single stream of type `R`, containing all values from all inner streams, as they come.  
`flatMap()` merges the emissions of these Observables, so that they may _interleave_.

Why `flatMap()` cannot give any guarantee about what order of those subevents will arrive at the downstream operator/subscriber?  
What `flatMap()` does is take a master sequence (Observable) of values appearing over time (events) and replaces each of the events with an independent subsequence.  
We no longer have a single the master sequence but a set of Observables, _each working on its own_, coming and going over time.

Use `flatMap()` for this condition:  
1. The result of transformation in map() must be an Observable. For example, performing long-running, asynchronous operation on each element of the stream without blocking.  
2. We need a one-to-many transformation, a single event is expanded into multiple sub-events. For example, a stream of customers is translated into streams of their orders, for which each customer can have an arbitrary number of orders.

We don't replace map() and filter() with flatMap() due to the clarity of code and performance.

How to handle a method returning an Iterable (like List or Set)?  
Create a pipeline using `flatMap()` or using `flatMapIterable()`.

Watch out when simply wrapping methods in an Observable!  
If the method was not a simple getter but an expensive operation in terms of run time, it is better to implement the method to explicitly return Observable<Order>.

`flatMap()` can react not only to events, but on any notification, namely events, errors, and completion.

Postponing events using `delay()`  
![delay](https://github.com/bluething/exercisereactive/blob/main/images/delaydiagram.png?raw=true)  
`delay()` basically takes an upstream Observable and shifts all events further in time.  
`delay()` shifts every single event further by a given amount of time, whereas `timer()` simply "sleeps" and emits a special event after given time.