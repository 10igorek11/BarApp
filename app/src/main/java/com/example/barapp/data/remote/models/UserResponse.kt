package com.example.barapp.data.remote.models

data class UserResponse(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String?=null,
    val phone: String = "",
    val password: String = "",
    val roleId: Int = 0,
)
