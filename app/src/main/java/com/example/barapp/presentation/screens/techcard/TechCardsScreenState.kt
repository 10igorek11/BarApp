package com.example.barapp.presentation.screens.techcard

import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.models.TechCardDto

data class TechCardsScreenState(
    var techCards:List<TechCardDto> = emptyList(),
    var cocktailTypes:List<CocktailType> = emptyList(),
    var searchText:String = "",
    var selectedType:CocktailType? = null,
    var errorStatus:Boolean = false,
    var isLoading:Boolean = true
)
