package com.jetbrains.kmpapp.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jetbrains.kmpapp.core.APIResponse
import com.jetbrains.kmpapp.data.model.MuseumObject
import com.jetbrains.kmpapp.domain.FetchObjectWithIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class ViewState(
    val isLoading: Boolean = false,
    val data: MuseumObject? = null,
    val errorMessage: String? = null
)

class DetailViewModel(private val fetchObjectWithIdUseCase: FetchObjectWithIdUseCase) : ViewModel() {

    private val _objectViewState = MutableStateFlow(ViewState())
    val objectViewState: StateFlow<ViewState> get() = _objectViewState

    fun fetchObjectId(objectId: Int) {
        viewModelScope.launch {
            fetchObjectWithIdUseCase(objectId).collect { response ->
                print(response)
                _objectViewState.value = when (response) {
                    is APIResponse.Loading -> _objectViewState.value.copy(isLoading = response.isLoading)
                    is APIResponse.Success -> _objectViewState.value.copy(
                        isLoading = false,
                        data = response.data
                    )

                    is APIResponse.Error -> _objectViewState.value.copy(
                        isLoading = false,
                        errorMessage = response.message
                    )
                }
            }
        }
    }
}
