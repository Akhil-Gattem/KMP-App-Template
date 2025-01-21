package com.jetbrains.kmpapp.domain

import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import kotlinx.coroutines.flow.Flow

interface MuseumRepository {
    fun fetchObjects(): Flow<APIResponse<List<MuseumObject>>>
}