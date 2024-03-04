package com.example.barapp.domain.models

data class AnswerDto(
    var id: Int = 0,
    var text: String = "",
    var isCorrect: Boolean = false,
    var questionId: Int = 0,
    var isDeleted: Boolean = false
)
