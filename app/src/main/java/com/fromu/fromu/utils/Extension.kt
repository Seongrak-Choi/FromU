package com.fromu.fromu.utils

import android.view.View
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

object Extension {
    fun View.clicks(): Flow<Unit> = callbackFlow {
        setOnClickListener {
            this.trySend(Unit)
        }
        awaitClose { setOnClickListener(null) }
    }

    fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> = flow {
        var lastEmissionTime = 0L
        collect { upstream ->
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastEmissionTime > windowDuration) {
                lastEmissionTime = currentTime
                emit(upstream)
            }
        }
    }

    fun View.setThrottleClick(uiScope: CoroutineScope, windowDuration: Long = 1000, onClick: () -> Unit) {
        clicks()
            .throttleFirst(windowDuration)
            .onEach { onClick.invoke() }
            .launchIn(uiScope)
    }

    fun EditText.debounce(time: Long = 500L, coroutineScope: CoroutineScope, block: (String) -> Unit) {
        var job: Job? = null
        doAfterTextChanged {
            job?.cancel()
            job = coroutineScope.launch {
                delay(time)
                block(it.toString())
            }
        }
    }
}