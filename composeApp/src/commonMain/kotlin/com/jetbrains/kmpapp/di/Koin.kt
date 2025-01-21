package com.jetbrains.kmpapp.di

import com.jetbrains.kmpapp.data.network.APIClient
import com.jetbrains.kmpapp.data.repository.MuseumRepositoryImpl
import com.jetbrains.kmpapp.domain.FetchObjectWithIdUseCase
import com.jetbrains.kmpapp.domain.FetchObjectsUseCase
import com.jetbrains.kmpapp.domain.MuseumRepository
import com.jetbrains.kmpapp.screens.detail.DetailViewModel
import com.jetbrains.kmpapp.screens.list.ListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                }, contentType = ContentType.Text.Plain)
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = Logger.DEFAULT
            }
            defaultRequest {
                header("Content-Type", "application/json")
            }
        }
    }

    single { APIClient(get(), baseURL = "https://raw.githubusercontent.com/") }
    single<MuseumRepository> { MuseumRepositoryImpl(get()) }
    single { FetchObjectsUseCase(get()) }
    single { FetchObjectWithIdUseCase(get()) }
}

val viewModelModule = module {
    factoryOf(::ListViewModel)
    factoryOf(::DetailViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
