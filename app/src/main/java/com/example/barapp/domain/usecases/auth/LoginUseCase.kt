package com.example.barapp.domain.usecases.auth

import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(login: String, password: String) = userRepository.authUser(login, password)
}