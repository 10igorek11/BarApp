package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.QuestionDtoResponse
import com.example.barapp.domain.models.QuestionDto

fun QuestionDtoResponse.toModel(): QuestionDto {
    return QuestionDto(
        id = this.id,
        text = this.text,
        photo = this.photo,
        testId = this.testId,
        isDeleted = this.isDeleted,
        answers = this.answers.map { it.toModel() },
    )
}

fun QuestionDto.toResponseModel(): QuestionDtoResponse {
    return QuestionDtoResponse(
        id = this.id,
        text = this.text,
        photo = this.photo,
        testId = this.testId,
        isDeleted = this.isDeleted,
        answers = this.answers.map { it.toResponseModel() },
    )
}
