package com.fromu.fromu.model.listener

interface ResourceSuccessListener<T> {
    fun onSuccess(res: T)
}