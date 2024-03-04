package com.example.barapp.data.remote.models

data class RecipeResponse(
    val id: Int,
    val consumptionBrutto1g: String,
    val consumptionNetto1g: String,
    val product: String
)
