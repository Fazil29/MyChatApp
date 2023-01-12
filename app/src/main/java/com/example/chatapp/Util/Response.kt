package com.example.chatapp.Util

sealed class Response<T>(val data: T? = null, val message: String? = null) {
    class SUCCESS<T>(data: T?) : Response<T>(data = data)
    class LOADING<T>() : Response<T>()
    class ERROR<T>(message: String?) : Response<T>(message = message)
}