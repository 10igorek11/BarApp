package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.PhotoResponse
import com.example.barapp.data.remote.models.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {
    @FormUrlEncoded
    @POST("upload")
    suspend fun setPhoto(@Query("key") key:String, @Field("image") image: String): Response<PhotoResponse>

    companion object {
        const val PHOTO_URL = "https://api.imgbb.com/1/"
    }
}
