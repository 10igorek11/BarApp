package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.CocktailTypeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface CocktailTypeApi {
    @GET(COCKTAIL_TYPE_URL)
    suspend fun getCocktailTypes(): Response<List<CocktailTypeResponse>>

    @PUT("$COCKTAIL_TYPE_URL/{id}")
    suspend fun updateCocktailType(@Path("id") id: Int, @Body cocktailType: CocktailTypeResponse): Response<Any?>

    @POST(COCKTAIL_TYPE_URL)
    suspend fun createCocktailType(@Body cocktailType: CocktailTypeResponse): Response<CocktailTypeResponse>

    @DELETE("$COCKTAIL_TYPE_URL/{id}")
    suspend fun deleteCocktailType(@Path("id") id: Int): Response<Any?>

    companion object {
        const val COCKTAIL_TYPE_URL = "cocktailTypes"
    }
}
