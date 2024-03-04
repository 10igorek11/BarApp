package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.TestDtoResponse
import com.example.barapp.data.remote.models.TestResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TestApi {
    @GET(TEST_URL)
    suspend fun getTests(): Response<List<TestResponse>>
    @GET("$TEST_URL/byuser/{id}")
    suspend fun getTestsByUser(@Path("id") id: Int): Response<List<TestResponse>>

    @GET("$TEST_URL/{id}")
    suspend fun getTest(@Path("id") id: Int): Response<TestDtoResponse>

    @PUT("$TEST_URL/{id}")
    suspend fun updateTest(@Path("id") id: Int, @Body test: TestDtoResponse): Response<Any?>

    @POST(TEST_URL)
    suspend fun createTest(@Body test: TestDtoResponse): Response<TestResponse>

    @DELETE("$TEST_URL/{id}")
    suspend fun deleteTest(@Path("id") id: Int): Response<Any?>

    companion object {
        const val TEST_URL = "tests"
    }
}
