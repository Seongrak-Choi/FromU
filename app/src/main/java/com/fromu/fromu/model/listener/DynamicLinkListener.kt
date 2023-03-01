package com.fromu.fromu.model.listener

interface DynamicLinkListener {
    fun onSuccess(deepLink: String)
    fun onFailure()
}