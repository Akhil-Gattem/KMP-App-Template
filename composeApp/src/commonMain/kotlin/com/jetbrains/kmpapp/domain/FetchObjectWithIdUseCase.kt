package com.jetbrains.kmpapp.domain

import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchObjectWithIdUseCase(private val repository: MuseumRepository) {
    operator fun invoke(objectId: Int): Flow<APIResponse<MuseumObject>> {
        return repository.fetchObjects().map { response ->
            when (response) {
                is APIResponse.Success -> {
                    val objectData = response.data?.find { it.objectID == objectId }
                    APIResponse.Success(objectData)
                }
                is APIResponse.Error -> {
                    APIResponse.Error(message = response.message.toString())
                }
                is APIResponse.Loading -> {
                    APIResponse.Loading(response.isLoading)
                }
            }
        }
    }
}
