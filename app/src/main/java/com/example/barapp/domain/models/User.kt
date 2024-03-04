package com.example.barapp.domain.models

data class User(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String? = null,
    val phone: String = "",
    val password: String = "",
    val roleId: Int = 0,
)
