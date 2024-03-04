package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.User
import com.example.barapp.domain.models.UserDto

interface UserRepository {
    suspend fun getUsers(): Resource<List<UserDto>>
    suspend fun getUser(id:Int): Resource<User>
    suspend fun authUser(login: String, password: String): Resource<User>
    suspend fun updateUser(id: Int, user: User): Resource<Any?>
    suspend fun createUser(user: User): Resource<User>
    suspend fun deleteUser(id: Int): Resource<Any?>
}