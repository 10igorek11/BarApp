package com.example.barapp.presentation.screens.techcard.techcard_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.techcard.GetTechCardByIdUseCase
import com.example.barapp.domain.usecases.techcardrecipe.GetTechCardRecipesUseCase
import com.example.barapp.presentation.screens.techcard.ScreenEvent
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
class TechCardScreenViewModel @Inject constructor(
    private val getTechCardByIdUseCase: GetTechCardByIdUseCase,
    private val getTechCardRecipesUseCase: GetTechCardRecipesUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(TechCardScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TechCardScreenState())
    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()

    fun onEvent(event: TechCardScreenEvent) {
        when (event) {
            is TechCardScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    if (event.id > 0) {
                        when (val techCard: Resource<TechCard> = getTechCardByIdUseCase(event.id)) {
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
                                when (val techCardRecipes = getTechCardRecipesUseCase(techCard.data!!.id)){
                                    is Resource.Error -> {
                                        screenEventChannel.send(
                                            ScreenEvent.ERROR
                                        )
                                    }
                                    is Resource.Loading -> {
                                        screenEventChannel.send(
                                            ScreenEvent.ERROR
                                        )
                                    }
                                    is Resource.Success -> {
                                        _state.update {
                                            it.copy(
                                                techCard = techCard.data,
                                                recipes = techCardRecipes.data!!,
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
        }
    }
}