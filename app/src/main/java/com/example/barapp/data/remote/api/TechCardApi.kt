package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.TechCardDtoResponse
import com.example.barapp.data.remote.models.TechCardResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TechCardApi {
    @GET(TECH_CARD_URL)
    suspend fun getTechCards(@Query("search") searchText:String, @Query("typeId") typeId:Int): Response<List<TechCardDtoResponse>>

    @GET("$TECH_CARD_URL/{id}")
    suspend fun getTechCard(@Path("id") id: Int): Response<TechCardResponse>

    @PUT("$TECH_CARD_URL/{id}")
    suspend fun updateTechCard(@Path("id") id: Int, @Body techCard: TechCardResponse): Response<Any?>

    @POST(TECH_CARD_URL)
    suspend fun createTechCard(@Body techCard: TechCardResponse): Response<TechCardResponse>

    @DELETE("$TECH_CARD_URL/{id}")
    suspend fun deleteTechCard(@Path("id") id: Int): Response<Any?>

    companion object {
        const val TECH_CARD_URL = "techCards"
    }
}
