A CallBack Function is a function that is passed into another function as an argument and is expected to execute after some kind of event.  
This type of design pattern is used in Observer Design Pattern.

##### Synchronous Callback

The callback performs all its work before returning to the call statement. The problem with synchronous callbacks are that they appear to lag.

When to use?  
Any process having multiple tasks where the tasks must be executed in sequence and doesn't occupy much time.

##### Asynchronous Callback

An Asynchronous call does not block the program from the code execution. When the call returns from the event, the call returns to the callback function.  
In Java, we use thread and invoke the callback method inside that thread. The callback function may be invoked from a thread but is not a requirement. A Callback may also start a new thread, thus making themselves asynchronous.

When to use?  
When the tasks are not dependent on each other and may take some time for execution