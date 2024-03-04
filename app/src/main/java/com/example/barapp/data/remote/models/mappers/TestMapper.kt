package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.TestDtoResponse
import com.example.barapp.data.remote.models.TestResponse
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto

fun TestDtoResponse.toModel(): TestDto {
    return TestDto(
        id = this.id,
        name = this.name,
        questions = this.questions.map { it.toModel() },
    )
}

fun TestDto.toResponseModel(): TestDtoResponse {
    return TestDtoResponse(
        id = this.id,
        name = this.name,
        questions = this.questions.map { it.toResponseModel() },
    )
}

fun TestResponse.toModel(): Test {
    return Test(
        id = this.id,
        name = this.name,
    )
}

fun Test.toResponseModel(): TestResponse {
    return TestResponse(
        id = this.id,
        name = this.name,
    )
}

