package com.nhn.coroutinesdemo

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_demo.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor

//Ref: https://github.com/Kotlin/kotlinx.coroutines/blob/master/ui/coroutines-guide-ui.md#event-conflation
class DemoCoroutineScopeActivity : CoroutineScopedAppActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)
        showTimerPerSecond()
        //runLongJobAndDisplayResultUi()
        runLongJobAndDisplayResultUiDirectly()
    }

    private fun showTimerPerSecond() = launch(Dispatchers.Main) {
        var second = 0
        while (true) {
            delay(1000)
            second ++
            tvTimer.text = second.toString()
        }
    }

    private fun runLongJobAndDisplayResultUiDirectly() = launch(Dispatchers.Main) {
        val fib: Int = withContext(Dispatchers.IO) {
            fibBlocking(45)
        }

        withContext(Dispatchers.Main) {
            tvHello.text = fib.toString()
        }
    }

    private fun runLongJobAndDisplayResultUi() = launch(Dispatchers.Main) {
        val fib: Int = fib(45)
        tvHello.text = fib.toString()
    }

    private fun runJob() = launch {
        for (i in 10 downTo 1) {
            tvHello.text = "Countdown $i ..."
            delay(1000)
        }
        tvHello.text = "Done!"
    }

    suspend fun fib(x: Int): Int = withContext(Dispatchers.IO) {
        fibBlocking(x)
    }

    fun fibBlocking(x: Int): Int =
        if (x <= 1) x else fibBlocking(x - 1) + fibBlocking(x - 2)
}
