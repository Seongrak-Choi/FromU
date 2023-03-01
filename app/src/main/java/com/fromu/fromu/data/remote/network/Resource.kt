package com.fromu.fromu.data.remote.network

sealed class Resource<out T> {
    data class Success<out T>(val body: T) : Resource<T>()
    data class Failed(val message: String) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
