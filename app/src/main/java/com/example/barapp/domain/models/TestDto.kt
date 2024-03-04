package com.example.barapp.domain.models

data class TestDto(
    val id: Int = 0,
    val name: String = "",
    val questions: List<QuestionDto> = emptyList(),
)
