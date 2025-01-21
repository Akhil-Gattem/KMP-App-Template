package com.jetbrains.kmpapp.core

import io.ktor.client.statement.HttpResponse


sealed class APIResponse<T> (val data: T? = null, val message: String? = null) {
    class Success<T>(data: T?) : APIResponse<T>(data)
    class Error<T>(message: String, data: T? = null) : APIResponse<T>(data,message)
    class Loading<T>(val isLoading: Boolean = true) : APIResponse<T>(null,)
}

sealed class APIResponseInternal<T> {
    data class Success<T>(val data: T) : APIResponseInternal<T>()
    class Unauthorized<T> : APIResponseInternal<T>()
    data class Other<T>(val response: HttpResponse?) : APIResponseInternal<T>()
    class Cancelled<T> : APIResponseInternal<T>()
}

