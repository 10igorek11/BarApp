package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.PassingStatusResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PassingStatusApi {
    @GET("$PASSING_STATUS_URL/byuser/{id}")
    suspend fun getPassingStatuses(@Path("id") id:Int): Response<List<PassingStatusResponse>>

    @POST(PASSING_STATUS_URL)
    suspend fun createPassingStatus(@Body passingStatus: PassingStatusResponse): Response<PassingStatusResponse>
    companion object {
        const val PASSING_STATUS_URL = "passingStatuses"
    }
}
