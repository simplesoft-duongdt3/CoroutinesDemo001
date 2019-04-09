package com.nhn.coroutinesdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

//Ref: https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#event-conflation
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //simple launch, auto launch, cancel when click
        //demo1()

        //run when clicked (multi click -> parallel run)
        //demo2()

        //manager actor with capacity = Channel.RENDEZVOUS (only run 1 action 1 time)
        //demo3()

        //manager actor with capacity = Channel.CONFLATED (only run 1 action + accept 1 next action)
        //demo4()

        //manager actor with capacity = Channel.UNLIMITED (run 1 action + accept all next action)
        //demo5()
    }

    private fun demo5() {
        tvHello.onClickDemo5 {
            for (i in 10 downTo 1) {
                tvHello.text = "Countdown $i ..."
                delay(1000)
            }
            tvHello.text = "Done!"
        }
    }

    private fun demo4() {
        tvHello.onClickDemo4 {
            for (i in 10 downTo 1) {
                tvHello.text = "Countdown $i ..."
                delay(1000)
            }
            tvHello.text = "Done!"
        }
    }

    private fun demo3() {
        tvHello.onClickDemo3 {
            for (i in 10 downTo 1) {
                tvHello.text = "Countdown $i ..."
                delay(1000)
            }
            tvHello.text = "Done!"
        }
    }

    private fun demo2() {
        tvHello.onClickDemo2 {
            for (i in 10 downTo 1) {
                tvHello.text = "Countdown $i ..."
                delay(1000)
            }
            tvHello.text = "Done!"
        }
    }

    private fun demo1() {
        val job = GlobalScope.launch(Dispatchers.Main) {
            for (i in 10 downTo 1) {
                tvHello.text = "Countdown $i ..."
                delay(1000)
            }
            tvHello.text = "Done!"
        }

        tvHello.setOnClickListener {
            job.cancel()
        }
    }

    fun View.onClickDemo2(action: suspend () -> Unit) {
        setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                action()
            }
        }
    }

    // Try clicking repeatedly on a circle in this version of the code.
    //  The clicks are just ignored while the countdown animation is running.
    //  This happens because the actor is busy with an animation and does not receive from its channel.
    //  By default, an actor's mailbox is backed by RendezvousChannel, whose offer operation succeeds only when the receive is active.
    fun View.onClickDemo3(action: suspend (View) -> Unit) {
        // launch one actor
        //default capacity = Channel.RENDEZVOUS
        val eventActor = GlobalScope.actor<View>(Dispatchers.Main) {
            for (event in channel) {
                action(event)
            }
        }

        // install a listener to activate this actor
        setOnClickListener {
            eventActor.offer(it)
        }
    }

    // capacity = Channel.CONFLATED
    // Now, if a View is clicked while the animation is running, it restarts animation after the end of it. Just once.
    // Repeated clicks while the animation is running are conflated and only the most recent event gets to be processed.
    fun View.onClickDemo4(action: suspend (View) -> Unit) {
        // launch one actor
        val eventActor = GlobalScope.actor<View>(Dispatchers.Main, capacity = Channel.CONFLATED) {
            for (event in channel) {
                action(event)
            }
        }

        // install a listener to activate this actor
        setOnClickListener {
            eventActor.offer(it)
        }
    }

    // capacity = Channel.UNLIMITED
    // Setting capacity = Channel.UNLIMITED creates a coroutine with LinkedListChannel mailbox that buffers all events.
    // In this case, the animation runs as many times as the circle is clicked.
    fun View.onClickDemo5(action: suspend (View) -> Unit) {
        // launch one actor
        val eventActor = GlobalScope.actor<View>(Dispatchers.Main, capacity = Channel.UNLIMITED) {
            for (event in channel) {
                action(event)
            }
        }

        // install a listener to activate this actor
        setOnClickListener {
            eventActor.offer(it)
        }
    }
}
