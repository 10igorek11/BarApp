package com.example.barapp.domain.usecases.auth

import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.AuthRepository
import javax.inject.Inject

class GetLoggedInUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getLoggedInUser()
}