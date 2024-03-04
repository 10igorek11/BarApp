package com.example.barapp.presentation.screens.techcard

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.cocktailtype.CreateCocktailTypeUseCase
import com.example.barapp.domain.usecases.cocktailtype.DeleteCocktailTypeUseCase
import com.example.barapp.domain.usecases.cocktailtype.GetCocktailTypesUseCase
import com.example.barapp.domain.usecases.cocktailtype.GetDefaultCocktailTypeUseCase
import com.example.barapp.domain.usecases.cocktailtype.UpdateCocktailTypeUseCase
import com.example.barapp.domain.usecases.techcard.GetTechCardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TechCardsScreenViewModel @Inject constructor(
    private val getTechCardsUseCase: GetTechCardsUseCase,
    private val getCocktailTypesUseCase: GetCocktailTypesUseCase,
    private val getDefaultCocktailTypeUseCase: GetDefaultCocktailTypeUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TechCardsScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TechCardsScreenState())

    init {
        loadTypes()
        loadTechCards()
    }
    private fun loadTypes() {
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
        }
    }
    private fun loadTechCards(){
        viewModelScope.launch {
            getLoggedInUserUseCase()?.let{_ ->
                when (val techCards = getTechCardsUseCase(state.value.searchText, state.value.selectedType?.id?:0)){
                    is Resource.Error -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorStatus = true
                            )
                        }
                    }
                    is Resource.Loading -> return@launch
                    is Resource.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorStatus = false,
                                techCards = techCards.data!!
                            )
                        }
                    }
                }
            }

        }
    }

    fun getLoggedInUser() = getLoggedInUserUseCase()
    fun onEvent(event:TechCardsScreenEvent) {
        when(event){
            TechCardsScreenEvent.LoadData -> {
                _state.update {
                    it.copy(
                        errorStatus = false,
                        isLoading = true,
                    )
                }
                loadTypes()
                loadTechCards()
            }

            is TechCardsScreenEvent.SearchTextChanged -> {
                _state.update {
                    it.copy(
                        searchText = event.text
                    )
                }
                loadTechCards()

            }
            is TechCardsScreenEvent.SelectedTypeChanged -> {
                _state.update {
                    it.copy(
                        selectedType = event.type
                    )
                }
                loadTechCards()

            }
        }
    }

}