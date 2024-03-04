package com.example.barapp.presentation.screens.techcard.type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.cocktailtype.CreateCocktailTypeUseCase
import com.example.barapp.domain.usecases.cocktailtype.DeleteCocktailTypeUseCase
import com.example.barapp.domain.usecases.cocktailtype.GetCocktailTypesUseCase
import com.example.barapp.domain.usecases.cocktailtype.UpdateCocktailTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailTypesScreenViewModel @Inject constructor(
    private val getCocktailTypesUseCase: GetCocktailTypesUseCase,
    private val createCocktailTypeUseCase: CreateCocktailTypeUseCase,
    private val updateCocktailTypeUseCase: UpdateCocktailTypeUseCase,
    private val deleteCocktailTypeUseCase: DeleteCocktailTypeUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(CocktailTypesScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), CocktailTypesScreenState())

    init {
        loadCocktailTypes()
    }

    private fun loadCocktailTypes() {
        viewModelScope.launch {
            when (val types:Resource<List<CocktailType>> = getCocktailTypesUseCase()) {
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
                            types = types.data!!
                        )
                    }
                }
            }


        }
    }

    fun onEvent(event: CocktailTypesScreenEvent) {
        when (event) {
            CocktailTypesScreenEvent.OnLoad -> {
                loadCocktailTypes()
            }
            is CocktailTypesScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    deleteCocktailTypeUseCase(event.typeId)
                    loadCocktailTypes()
                }
            }
            is CocktailTypesScreenEvent.OnChange -> {
                viewModelScope.launch {
                    updateCocktailTypeUseCase(event.type)
                    loadCocktailTypes()
                }
            }
            is CocktailTypesScreenEvent.OnCreate -> {
                viewModelScope.launch {
                    createCocktailTypeUseCase(event.type)
                    loadCocktailTypes()
                }
            }
        }
    }

    fun getLoggedInUser() = getLoggedInUserUseCase()

}