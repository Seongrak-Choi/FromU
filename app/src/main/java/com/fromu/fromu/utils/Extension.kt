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

    /**
     * 클릭 후 1초 동안 재동작 안 하도록 throttle 메소드
     *
     * @param uiScope
     * @param windowDuration
     * @param onClick
     */
    fun View.setThrottleClick(uiScope: CoroutineScope, windowDuration: Long = 1000, onClick: () -> Unit) {
        clicks()
            .throttleFirst(windowDuration)
            .onEach { onClick.invoke() }
            .launchIn(uiScope)
    }

    /**
     * EditText 입력 시 0.5초 후 원하는 작업 실행 되도록 debounce 해주는 확장 함수
     *
     * @param time
     * @param coroutineScope
     * @param block
     */
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