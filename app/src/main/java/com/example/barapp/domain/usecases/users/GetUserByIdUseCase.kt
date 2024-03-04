package com.example.barapp.domain.usecases.users

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.User
import com.example.barapp.domain.models.UserDto
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(id:Int):Resource<User> = userRepository.getUser(id)
}