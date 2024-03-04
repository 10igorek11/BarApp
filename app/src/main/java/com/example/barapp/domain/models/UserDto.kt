package com.example.barapp.domain.models

data class UserDto(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String? = null,
    val roleId: Int = 0,
    val passedTests:List<Test> = emptyList()
)
