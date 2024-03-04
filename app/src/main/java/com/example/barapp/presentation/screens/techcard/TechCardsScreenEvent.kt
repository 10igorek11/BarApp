package com.example.barapp.presentation.screens.techcard

import com.example.barapp.domain.models.CocktailType

sealed class TechCardsScreenEvent{
    object LoadData: TechCardsScreenEvent()
    data class SearchTextChanged(val text: String): TechCardsScreenEvent()
    data class SelectedTypeChanged(val type: CocktailType?): TechCardsScreenEvent()
}
