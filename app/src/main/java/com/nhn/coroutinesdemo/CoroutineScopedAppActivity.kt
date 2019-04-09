package com.nhn.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class CoroutineScopedAppActivity: AppCompatActivity(), CoroutineScope by MainScope() {

    override fun onDestroy() {
        super.onDestroy()
        this.cancel()
    }
}