package com.example.barapp.presentation.screens.learning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.learningmaterial.GetLearningMaterialsUseCase
import com.example.barapp.domain.usecases.passingstatus.GetPassingStatusesUseCase
import com.example.barapp.presentation.screens.tests.LoadingEvent
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
class LearningMaterialsScreenViewModel @Inject constructor(
    private val getLearningMaterialsUseCase: GetLearningMaterialsUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(LearningMaterialsScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), LearningMaterialsScreenState())

    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()

    private fun loadLearningMaterials(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    errorStatus = false,
                    isLoading = true,
                )
            }
            getLoggedInUserUseCase()?.let{_ ->
                when (val materials = getLearningMaterialsUseCase()){
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorStatus = true
                            )
                        }
                        screenEventChannel.send(
                            ScreenEvent.ERROR
                        )
                    }
                    is Resource.Loading -> return@launch
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorStatus = false,
                                materials = materials.data!!
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

    fun getLoggedInUser() = getLoggedInUserUseCase()
    fun onEvent(event:LearningMaterialsScreenEvent) {
        when(event){
            LearningMaterialsScreenEvent.LoadData -> {
                loadLearningMaterials()
            }

            LearningMaterialsScreenEvent.Refresh -> {
                viewModelScope.launch {
                    screenEventChannel.send(
                        ScreenEvent.DEFAULT
                    )
                }
            }
        }
    }

}