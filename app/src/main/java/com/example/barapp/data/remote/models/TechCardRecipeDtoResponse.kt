package com.example.barapp.data.remote.models

data class TechCardRecipeDtoResponse(
    val id: Int,
    val techCardId: Int,
    val recipeId: Int,
    val recipe: RecipeResponse
)
