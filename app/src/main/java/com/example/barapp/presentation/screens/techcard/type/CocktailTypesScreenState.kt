package com.example.barapp.presentation.screens.techcard.type

import com.example.barapp.domain.models.CocktailType

data class CocktailTypesScreenState(
    var types:List<CocktailType> = emptyList(),
    var errorStatus:Boolean = false,
    var isLoading:Boolean = true
)
