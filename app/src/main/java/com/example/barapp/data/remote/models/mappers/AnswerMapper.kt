package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.AnswerDtoResponse
import com.example.barapp.domain.models.AnswerDto

fun AnswerDtoResponse.toModel(): AnswerDto {
    return AnswerDto(
        id = this.id,
        text = this.text,
        isCorrect = this.isCorrect,
        questionId = this.questionId,
        isDeleted = this.isDeleted,
    )
}
fun AnswerDto.toResponseModel(): AnswerDtoResponse {
    return AnswerDtoResponse(
        id = this.id,
        text = this.text,
        isCorrect = this.isCorrect,
        questionId = this.questionId,
        isDeleted = this.isDeleted,
    )
}
