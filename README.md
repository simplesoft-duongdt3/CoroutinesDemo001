# CoroutinesDemo001
Coroutines Kotlin Simple Demo
Ref: https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#event-conflation


Learned:

1. Launch UI coroutine
+ GlobalScope.launch(Dispatchers.Main)
+ delay

2. Cancel UI coroutine
+ val job = GlobalScope.launch(Dispatchers.Main) {}
+ job.cancel()

 Notice: 
 It just signals the coroutine to cancel its job, without waiting for it to actually terminate. 
 It can be invoked from anywhere. 
 Invoking it on a coroutine that was already cancelled or has completed does nothing.
 
 
3. Using actors within UI context
val eventActor = GlobalScope.actor<MouseEvent>(Dispatchers.Main)
+ capacity = Channel.RENDEZVOUS (only run 1 action 1 time) (only run 1 action 1 time)
+ capacity = Channel.CONFLATED (only run 1 action + accept 1 next action)
+ capacity = Channel.UNLIMITED (run 1 action + accept all next action)

4. Starting coroutine in UI event handlers without dispatch
+ GlobalScope.launch(Dispatchers.Main, CoroutineStart.UNDISPATCHED)
