package com.example.barapp.domain.usecases

import com.example.barapp.domain.models.Photo
import com.example.barapp.domain.repositories.PhotoRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class SetPhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(base64photo: String) = photoRepository.setPhoto(base64photo)
}