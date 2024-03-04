package com.example.barapp.presentation.screens.techcard.type

import android.content.Context
import com.example.barapp.domain.models.AnswerDto
import com.example.barapp.domain.models.CocktailType

sealed class CocktailTypesScreenEvent {
    data class OnDelete(val typeId: Int): CocktailTypesScreenEvent()
    data class OnChange(val type: CocktailType): CocktailTypesScreenEvent()
    data class OnCreate(val type: CocktailType): CocktailTypesScreenEvent()
    object OnLoad: CocktailTypesScreenEvent()
}