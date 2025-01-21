package com.jetbrains.kmpapp.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import com.jetbrains.kmpapp.domain.FetchObjectsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ViewState(
    val isLoading: Boolean = false,
    val data: List<MuseumObject> = emptyList(),
    val errorMessage: String? = null
)

class ListViewModel(private val fetchObjectsUseCase: FetchObjectsUseCase) : ViewModel() {

    private val _objectsViewState = MutableStateFlow(ViewState())
    val objectsViewState: StateFlow<ViewState> get() = _objectsViewState

    init {
        fetchObjects()
    }

    private fun fetchObjects() {
        viewModelScope.launch {
            fetchObjectsUseCase().collect { response ->
                print(response)
                _objectsViewState.value = when (response) {
                    is APIResponse.Loading -> _objectsViewState.value.copy(isLoading = response.isLoading)
                    is APIResponse.Success -> _objectsViewState.value.copy(
                        isLoading = false,
                        data = response.data ?: emptyList()
                    )

                    is APIResponse.Error -> _objectsViewState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            }
        }
    }
}
