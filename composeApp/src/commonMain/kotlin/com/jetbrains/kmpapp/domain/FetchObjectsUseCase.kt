package com.jetbrains.kmpapp.domain

import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FetchObjectsUseCase(private val repository: MuseumRepository) {
    operator fun invoke(): Flow<APIResponse<List<MuseumObject>>> {
        return repository.fetchObjects().map { response ->
            when (response) {
                is APIResponse.Success -> {
                    val filteredData = response.data?.filter { it.title.isNotEmpty() }
                    APIResponse.Success(filteredData)
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

