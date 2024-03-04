package com.example.barapp.data.remote.models

data class UserDtoResponse(
    val id: Int = 0,
    val firstName: String = "",
    val lastName: String = "",
    val middleName: String? = null,
    val roleId: Int = 0,
    val passedTests: List<TestResponse>
)
