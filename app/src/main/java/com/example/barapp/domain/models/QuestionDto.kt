package com.example.barapp.domain.models

data class QuestionDto(
    var id: Int = 0,
    var text: String = "",
    var photo: String? = null,
    var testId: Int = 0,
    var isDeleted: Boolean = false,
    var answers: List<AnswerDto> = emptyList()
)
