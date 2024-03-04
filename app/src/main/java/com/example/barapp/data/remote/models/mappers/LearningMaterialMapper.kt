package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.LearningMaterialDtoResponse
import com.example.barapp.data.remote.models.LearningMaterialResponse
import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto

fun LearningMaterialDtoResponse.toModel(): LearningMaterialDto {
    return LearningMaterialDto(
        id = this.id,
        title = this.title,
        preview = this.preview,
        text = this.text,
        contents = this.contents.map { it.toModel() },
    )
}

fun LearningMaterialDto.toResponseModel(): LearningMaterialDtoResponse {
    return LearningMaterialDtoResponse(
        id = this.id,
        title = this.title,
        preview = this.preview,
        text = this.text,
        contents = this.contents.map { it.toResponseModel() },
    )
}
fun LearningMaterialResponse.toModel(): LearningMaterial {
    return LearningMaterial(
        id = this.id,
        title = this.title,
        preview = this.preview,
        text = this.text,
    )
}

fun LearningMaterial.toResponseModel(): LearningMaterialResponse {
    return LearningMaterialResponse(
        id = this.id,
        title = this.title,
        preview = this.preview,
        text = this.text,
    )
}