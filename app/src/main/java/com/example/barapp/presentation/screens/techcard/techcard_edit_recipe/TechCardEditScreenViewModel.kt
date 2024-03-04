package com.example.barapp.presentation.screens.techcard.techcard_edit_recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.R
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.usecases.cocktailtype.GetCocktailTypesUseCase
import com.example.barapp.domain.usecases.cocktailtype.GetDefaultCocktailTypeUseCase
import com.example.barapp.domain.usecases.techcard.CreateTechCardUseCase
import com.example.barapp.domain.usecases.techcard.DeleteTechCardUseCase
import com.example.barapp.domain.usecases.techcard.GetTechCardByIdUseCase
import com.example.barapp.domain.usecases.techcard.UpdateTechCardUseCase
import com.example.barapp.domain.usecases.techcardrecipe.CreateTechCardRecipeUseCase
import com.example.barapp.domain.usecases.techcardrecipe.DeleteTechCardRecipeUseCase
import com.example.barapp.domain.usecases.techcardrecipe.GetTechCardRecipesUseCase
import com.example.barapp.domain.usecases.techcardrecipe.UpdateTechCardRecipeUseCase
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
class TechCardEditScreenViewModel @Inject constructor(
    private val getTechCardByIdUseCase: GetTechCardByIdUseCase,
    private val deleteTechCardUseCase: DeleteTechCardUseCase,
    private val createTechCardUseCase: CreateTechCardUseCase,
    private val updateTechCardUseCase: UpdateTechCardUseCase,
    private val getTechCardRecipesUseCase: GetTechCardRecipesUseCase,
    private val deleteTechCardRecipeUseCase: DeleteTechCardRecipeUseCase,
    private val createTechCardRecipeUseCase: CreateTechCardRecipeUseCase,
    private val updateTechCardRecipeUseCase: UpdateTechCardRecipeUseCase,
    private val getDefaultCocktailTypeUseCase: GetDefaultCocktailTypeUseCase,
    private val getCocktailTypesUseCase: GetCocktailTypesUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TechCardEditScreenState())
    var state = _state.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        TechCardEditScreenState()
    )
    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()
    fun onEvent(event: TechCardEditScreenEvent) {
        when (event) {
            is TechCardEditScreenEvent.OnLoad -> {
                viewModelScope.launch {

                    val cocktailTypesList = arrayListOf(getDefaultCocktailTypeUseCase())
                    when (val cocktailTypes = getCocktailTypesUseCase()){
                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    cocktailTypes = cocktailTypesList
                                )
                            }
                        }
                        is Resource.Loading -> return@launch
                        is Resource.Success -> {
                            cocktailTypesList.addAll(cocktailTypes.data!!)
                            _state.update {
                                it.copy(
                                    cocktailTypes = cocktailTypesList
                                )
                            }
                        }
                    }
                    if (event.id > 0) {
                        when (val techCard: Resource<TechCard> =
                            getTechCardByIdUseCase(event.id)) {
                            is Resource.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false
                                    )
                                }
                                screenEventChannel.send(
                                    ScreenEvent.ERROR
                                )
                            }

                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                when (val recipes =
                                    getTechCardRecipesUseCase(techCardId = techCard.data!!.id)) {
                                    is Resource.Error -> {
                                        _state.update {
                                            it.copy(
                                                isLoading = false
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
                                                techCard = techCard.data,
                                                initialRecipes = recipes.data!!,
                                                recipes = recipes.data,
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
                    } else {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                recipes = emptyList(),
                                techCard = TechCard(
                                    name = event.context.getString(R.string.techcard_num_one)
                                )
                            )
                        }
                        screenEventChannel.send(
                            ScreenEvent.SUCCESS
                        )
                    }
                }
            }

            is TechCardEditScreenEvent.OnConfirm -> {
                viewModelScope.launch {
                    var isError = false
                    if (_state.value.techCard.applicationScope.trim().isEmpty()) {
                        _state.update {
                            it.copy(
                                applicationScopeError = event.context.getString(R.string.required_field_error)
                            )
                        }
                        isError = true
                    }
                    if (_state.value.techCard.rawMaterialRequirements.trim().isEmpty()) {
                        _state.update {
                            it.copy(
                                rawMaterialRequirementsError = event.context.getString(R.string.required_field_error)
                            )
                        }
                        isError = true
                    }
                    if (_state.value.techCard.technologicalProcess.trim().isEmpty()) {
                        _state.update {
                            it.copy(
                                technologicalProcessError = event.context.getString(R.string.required_field_error)
                            )
                        }
                        isError = true
                    }
                    if (_state.value.recipes.isEmpty()) {
                        _state.update {
                            it.copy(
                                recipesError = event.context.getString(R.string.no_recipes_added)
                            )
                        }
                        isError = true
                    }
                    if (isError) return@launch

                    if (_state.value.techCard.id == 0) {
                        when (val result = createTechCardUseCase(_state.value.techCard)) {
                            is Resource.Error -> {
                                return@launch
                            }

                            is Resource.Loading -> {
                                return@launch
                            }

                            is Resource.Success -> {
                                _state.value.recipes.forEach {
                                    when (createTechCardRecipeUseCase(
                                        it.copy(
                                            techCardId = result.data!!.id
                                        )
                                    )) {
                                        is Resource.Error -> {
                                            screenEventChannel.send(
                                                ScreenEvent.ERROR
                                            )
                                            return@launch
                                        }

                                        is Resource.Loading -> {
                                            screenEventChannel.send(
                                                ScreenEvent.ERROR
                                            )
                                            return@launch
                                        }

                                        is Resource.Success -> {
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        when (updateTechCardUseCase(_state.value.techCard)) {
                            is Resource.Error -> {
                                return@launch
                            }

                            is Resource.Loading -> {
                                return@launch
                            }

                            is Resource.Success -> {
                                _state.value.recipes.forEach {
                                    if (it.id <= 0) {
                                        when (createTechCardRecipeUseCase(
                                            it.copy(
                                                techCardId = _state.value.techCard.id
                                            )
                                        )) {
                                            is Resource.Error -> {
                                                screenEventChannel.send(
                                                    ScreenEvent.ERROR
                                                )
                                                return@launch
                                            }

                                            is Resource.Loading -> return@launch

                                            is Resource.Success -> {
                                            }
                                        }
                                    }
                                    else {
                                        when (updateTechCardRecipeUseCase(
                                            it
                                        )) {
                                            is Resource.Error -> {
                                                screenEventChannel.send(
                                                    ScreenEvent.ERROR
                                                )
                                                return@launch
                                            }

                                            is Resource.Loading -> return@launch

                                            is Resource.Success -> {
                                            }
                                        }
                                    }

                                }

                                val deletedRecipes = _state.value.initialRecipes.filterNot { newItem -> _state.value.recipes.any { it.id == newItem.id } }
                                deletedRecipes.forEach {
                                    when (deleteTechCardRecipeUseCase(it.id)) {
                                        is Resource.Error -> {
                                            screenEventChannel.send(
                                                ScreenEvent.ERROR
                                            )
                                            return@launch
                                        }

                                        is Resource.Loading -> return@launch

                                        is Resource.Success -> {
                                        }
                                    }
                                }
                            }
                        }
                    }
                    screenEventChannel.send(
                        ScreenEvent.DEFAULT
                    )
                }
            }

            is TechCardEditScreenEvent.OnRecipeAdd -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            recipes = it.recipes.plus(event.techCardRecipeDto.copy(
                                id = 0 - _state.value.recipes.size
                            )),
                            recipesError = null,
                            isModified = true
                        )
                    }
                }
            }

            is TechCardEditScreenEvent.OnRecipeDelete -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            recipes = it.recipes.minus(event.techCardRecipeDto),
                            isModified = true
                        )
                    }
                }
            }


            is TechCardEditScreenEvent.OnApplicationScopeChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            techCard = it.techCard.copy(
                                applicationScope = event.text
                            ),
                            applicationScopeError = null,
                            isModified = true
                        )
                    }
                }
            }

            is TechCardEditScreenEvent.OnTechnologicalProcessChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            techCard = it.techCard.copy(
                                technologicalProcess = event.text
                            ),
                            technologicalProcessError = null,
                            isModified = true
                        )
                    }
                }
            }

            is TechCardEditScreenEvent.OnRawMaterialRequirementsChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            techCard = it.techCard.copy(
                                rawMaterialRequirements = event.text
                            ),
                            rawMaterialRequirementsError = null,
                            isModified = true
                        )
                    }
                }
            }

            is TechCardEditScreenEvent.OnNameChange -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            techCard = it.techCard.copy(
                                name = event.name
                            ),
                            isModified = true
                        )
                    }
                }
            }

            TechCardEditScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    if (_state.value.techCard.id > 0) {
                        deleteTechCardUseCase(_state.value.techCard.id)
                    }
                    screenEventChannel.send(ScreenEvent.DEFAULT)
                }
            }

            is TechCardEditScreenEvent.OnRecipeUpdate -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            recipes = it.recipes.map { techCardDto ->
                                if(techCardDto.id==event.techCardRecipeDto.id) event.techCardRecipeDto else techCardDto
                            },
                            recipesError = null,
                            isModified = true
                        )
                    }
                }

            }
            is TechCardEditScreenEvent.SelectedTypeChanged -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            techCard = it.techCard.copy(
                                typeId = event.type?.id?: 0
                            ),
                            isModified = true
                        )
                    }
                }
            }
        }
    }

}