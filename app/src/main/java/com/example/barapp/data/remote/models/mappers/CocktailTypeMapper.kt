package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.CocktailTypeResponse
import com.example.barapp.domain.models.CocktailType

fun CocktailTypeResponse.toModel(): CocktailType {
    return CocktailType(
        id = this.id,
        name = this.name,
    )
}

fun CocktailType.toResponseModel(): CocktailTypeResponse {
    return CocktailTypeResponse(
        id = this.id,
        name = this.name,
    )
}