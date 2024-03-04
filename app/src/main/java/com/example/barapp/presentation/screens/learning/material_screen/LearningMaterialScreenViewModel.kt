package com.example.barapp.presentation.screens.learning.material_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.learningmaterial.GetLearningMaterialByIdUseCase
import com.example.barapp.presentation.screens.learning.ScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LearningMaterialScreenViewModel @Inject constructor(
    private val getLearningMaterialByIdUseCase: GetLearningMaterialByIdUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LearningMaterialScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), LearningMaterialScreenState())
    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()

    fun onEvent(event: LearningMaterialScreenEvent) {
        when (event) {
            is LearningMaterialScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    if (event.id > 0) {
                        when (val material: Resource<LearningMaterialDto> = getLearningMaterialByIdUseCase(event.id)) {
                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                screenEventChannel.send(
                                    ScreenEvent.DEFAULT
                                )
                            }
                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        material = material.data!!,
                                        isLoading = false
                                    )
                                }
                                screenEventChannel.send(
                                    ScreenEvent.SUCCESS
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    fun getLoggedInLearningMaterialDto() = getLoggedInUserUseCase()

}