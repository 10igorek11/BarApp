package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.TechCardRecipeDtoResponse
import com.example.barapp.data.remote.models.TechCardRecipeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TechCardRecipeApi {
    @GET("$TECH_CARD_RECIPE_URL/{id}")
    suspend fun getTechCardRecipes(@Path("id") id: Int): Response<List<TechCardRecipeDtoResponse>>

    @PUT("$TECH_CARD_RECIPE_URL/{id}")
    suspend fun updateTechCardRecipe(@Path("id") id: Int, @Body techCardRecipe: TechCardRecipeDtoResponse): Response<Any?>

    @POST(TECH_CARD_RECIPE_URL)
    suspend fun createTechCardRecipe(@Body techCardRecipe: TechCardRecipeDtoResponse): Response<TechCardRecipeResponse>

    @DELETE("$TECH_CARD_RECIPE_URL/{id}")
    suspend fun deleteTechCardRecipe(@Path("id") id: Int): Response<Any?>

    companion object {
        const val TECH_CARD_RECIPE_URL = "techCardRecipes"
    }
}
