package com.example.barapp.domain.models

data class TechCardRecipeDto(
    val id: Int,
    val techCardId: Int,
    val recipeId: Int,
    val recipe: Recipe
)
