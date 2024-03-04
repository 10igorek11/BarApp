package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.TechCardRecipeDtoResponse
import com.example.barapp.data.remote.models.TechCardRecipeResponse
import com.example.barapp.domain.models.TechCardRecipe
import com.example.barapp.domain.models.TechCardRecipeDto

fun TechCardRecipeDtoResponse.toModel(): TechCardRecipeDto {
    return TechCardRecipeDto(
        id = this.id,
        techCardId = this.techCardId,
        recipeId = this.recipeId,
        recipe = this.recipe.toModel(),
    )
}

fun TechCardRecipeDto.toResponseModel(): TechCardRecipeDtoResponse {
    return TechCardRecipeDtoResponse(
        id = this.id,
        techCardId = this.techCardId,
        recipeId = this.recipeId,
        recipe = this.recipe.toResponseModel(),
    )
}

fun TechCardRecipeResponse.toModel(): TechCardRecipe {
    return TechCardRecipe(
        id = this.id,
        techCardId = this.techCardId,
        recipeId = this.recipeId,
    )
}

fun TechCardRecipe.toResponseModel(): TechCardRecipeResponse {
    return TechCardRecipeResponse(
        id = this.id,
        techCardId = this.techCardId,
        recipeId = this.recipeId,
    )
}

