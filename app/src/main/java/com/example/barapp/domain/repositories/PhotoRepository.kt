package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.Photo
import com.example.barapp.domain.models.User

interface PhotoRepository {
    suspend fun setPhoto(base64Image:String): Resource<Photo>
}