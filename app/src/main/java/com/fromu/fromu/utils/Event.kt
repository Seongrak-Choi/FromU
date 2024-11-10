package com.fromu.fromu.utils

import androidx.lifecycle.Observer

/*
ViewModel에 있는 LiveData를 Observe하고 있을 때, 생명주기를 다시타게 되면 View에서 관찰중인 LiveData를 다시 Observing하게 되면서,
코드가 반복되는 상황이 발생할 수 있다. 따라서, Even Wrapper Class를 이용하여 처리했던 데이터인지 아닌지 판단하여 같은 코드가 반복되지 않도록 처리할 수 있다.
 */
class Event<T>(private val content: T) {

    private var hasBeenHandled = false

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}

class EventObserver<T>(private val onEventUnHandledContent: (T) -> Unit) : Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        event.getContentIfNotHandled()?.let {
            onEventUnHandledContent(it)
        }
    }
}