package com.example.barapp.data.remote.models

data class TestDtoResponse(
    val id: Int = 0,
    val name: String = "",
    val questions: List<QuestionDtoResponse> = emptyList(),
)
