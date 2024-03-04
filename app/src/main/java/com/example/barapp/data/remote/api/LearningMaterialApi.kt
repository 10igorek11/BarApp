package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.LearningMaterialDtoResponse
import com.example.barapp.data.remote.models.LearningMaterialResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface LearningMaterialApi {
    @GET(LEARNING_MATERIAL_URL)
    suspend fun getLearningMaterials(): Response<List<LearningMaterialResponse>>

    @GET("$LEARNING_MATERIAL_URL/{id}")
    suspend fun getLearningMaterial(@Path("id") id: Int): Response<LearningMaterialDtoResponse>

    @PUT("$LEARNING_MATERIAL_URL/{id}")
    suspend fun updateLearningMaterial(@Path("id") id: Int, @Body learningMaterial: LearningMaterialDtoResponse): Response<Any?>

    @POST(LEARNING_MATERIAL_URL)
    suspend fun createLearningMaterial(@Body learningMaterial: LearningMaterialDtoResponse): Response<LearningMaterialResponse>

    @DELETE("$LEARNING_MATERIAL_URL/{id}")
    suspend fun deleteLearningMaterial(@Path("id") id: Int): Response<Any?>

    companion object {
        const val LEARNING_MATERIAL_URL = "learningmaterials"
    }
}
