package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.RecipeResponse
import com.example.barapp.domain.models.Recipe

fun RecipeResponse.toModel(): Recipe {
    return Recipe(
        id = this.id,
        consumptionBrutto1g = this.consumptionBrutto1g,
        consumptionNetto1g = this.consumptionNetto1g,
        product = this.product,
    )
}

fun Recipe.toResponseModel(): RecipeResponse {
    return RecipeResponse(
        id = this.id,
        consumptionBrutto1g = this.consumptionBrutto1g,
        consumptionNetto1g = this.consumptionNetto1g,
        product = this.product,
    )
}
