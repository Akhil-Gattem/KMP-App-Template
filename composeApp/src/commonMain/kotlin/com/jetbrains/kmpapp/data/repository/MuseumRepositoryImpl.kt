package com.jetbrains.kmpapp.data.repository

import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import com.jetbrains.kmpapp.data.network.APIClient
import com.jetbrains.kmpapp.domain.MuseumRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MuseumRepositoryImpl(private val apiClient: APIClient) : MuseumRepository {
    override fun fetchObjects(): Flow<APIResponse<List<MuseumObject>>> {
        return flow {
            emit(APIResponse.Loading(true))
            try {
                val data = apiClient.getData()
                emit(APIResponse.Success(data))
            } catch (e: Exception) {
                emit(APIResponse.Error("Failed to fetch data: ${e.message}"))
            } finally {
                emit(APIResponse.Loading(false))
            }
        }
    }


}

