package com.example.barapp.data.remote.models

data class AnswerDtoResponse(
    val id: Int,
    val text: String,
    val isCorrect: Boolean,
    val questionId: Int,
    val isDeleted: Boolean
)
