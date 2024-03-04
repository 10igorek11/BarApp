package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.UserDtoResponse
import com.example.barapp.data.remote.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @GET(USER_URL)
    suspend fun getUsers(): Response<List<UserDtoResponse>>

    @GET("$USER_URL/{id}")
    suspend fun getUser(@Path("id") id:Int): Response<UserResponse>

    @GET("$USER_URL/auth")
    suspend fun authUser(@Query("login") login: String, @Query("password") password: String): Response<UserResponse>

    @PUT("$USER_URL/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserResponse): Response<Any?>

    @POST(USER_URL)
    suspend fun createUser(@Body user: UserResponse): Response<UserResponse>

    @DELETE("$USER_URL/{id}")
    suspend fun deleteUser(@Path("id") id: Int): Response<Any?>

    companion object {
        const val USER_URL = "users"
    }
}
