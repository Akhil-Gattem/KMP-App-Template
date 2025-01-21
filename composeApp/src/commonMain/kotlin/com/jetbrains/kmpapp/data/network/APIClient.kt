package com.jetbrains.kmpapp.data.network

import com.jetbrains.kmpapp.core.APIResponseInternal
import com.jetbrains.kmpapp.data.model.MuseumObject
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


class APIClient(private val client: HttpClient, private val baseURL: String) : Client {

    private suspend inline fun <reified T> fetch(crossinline api: suspend () -> HttpResponse): APIResponseInternal<T> {
        try {
            val response = withContext(Dispatchers.IO) { api() }
            return when (response.status) {
                HttpStatusCode.OK -> APIResponseInternal.Success(response.body())
                HttpStatusCode.Unauthorized -> APIResponseInternal.Unauthorized()
                else -> APIResponseInternal.Other(response)
            }
        } catch (ce: kotlinx.coroutines.CancellationException) {
            return APIResponseInternal.Cancelled()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return APIResponseInternal.Other(null)
    }

    override suspend fun getData(): List<MuseumObject> {
        val response = fetch<List<MuseumObject>> {
            client.get("${baseURL}/Kotlin/KMP-App-Template/main/list.json")
        }
        return when (response) {
            is APIResponseInternal.Success -> {
                println("Data fetched successfully")
                response.data
            }
            is APIResponseInternal.Unauthorized -> {
                println("Unauthorized access detected. Check your credentials or token.")
                throw RuntimeException("Unauthorized access.")
            }
            is APIResponseInternal.Cancelled -> {
                println("Request was cancelled by the user or system.")
                throw RuntimeException("Request cancelled.")
            }
            is APIResponseInternal.Other -> {
                println("An unexpected response or error occurred: ${response.response?.status?.value}")
                throw RuntimeException("Unexpected error occurred.")
            }
        }
    }
}