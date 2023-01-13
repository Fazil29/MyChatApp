package com.example.chatapp.util

sealed class Response<T> {
    data class SUCCESS<T>(val data: T) : Response<T>()
    class LOADING<T> : Response<T>()
    data class ERROR<T>(val message: String) : Response<T>()
}