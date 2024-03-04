package com.example.barapp.domain.repositories

import com.example.barapp.domain.models.User

interface AuthRepository {
    fun getLoggedInUser(): User?
    fun setLoggedInUser(user: User?)
}