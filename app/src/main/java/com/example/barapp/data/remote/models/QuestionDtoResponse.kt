package com.example.barapp.data.remote.models

data class QuestionDtoResponse(
    val id: Int,
    val text: String,
    val photo: String?,
    val testId: Int,
    val isDeleted: Boolean,
    val answers: List<AnswerDtoResponse>
)
