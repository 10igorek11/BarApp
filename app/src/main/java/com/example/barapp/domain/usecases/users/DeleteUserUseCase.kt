package com.example.barapp.domain.usecases.users

import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id: Int) = userRepository.deleteUser(id)
}