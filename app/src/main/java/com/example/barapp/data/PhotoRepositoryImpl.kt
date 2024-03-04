package com.example.barapp.data

import com.example.barapp.BuildConfig
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.PhotoApi
import com.example.barapp.data.remote.api.UserApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.Photo
import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.PhotoRepository
import com.example.barapp.domain.repositories.UserRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val api: PhotoApi
) : PhotoRepository{
    override suspend fun setPhoto(base64Image: String): Resource<Photo> {
        val response = api.setPhoto(
            key = BuildConfig.IMGBB_API_KEY,
            image = base64Image
        )
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse.toModel())
            }
        }
        return Resource.Error(response.errorBody().toString())

    }

}