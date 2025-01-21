package com.jetbrains.kmpapp.data.network

import com.jetbrains.kmpapp.data.model.MuseumObject

interface Client {
    suspend fun getData(): List<MuseumObject>
}