package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.ContentDtoResponse
import com.example.barapp.domain.models.ContentDto

fun ContentDtoResponse.toModel(): ContentDto {
    return ContentDto(
        id = this.id,
        photo = this.photo,
        materialId = this.materialId,
        isDeleted = this.isDeleted,
    )
}

fun ContentDto.toResponseModel(): ContentDtoResponse {
    return ContentDtoResponse(
        id = this.id,
        photo = this.photo,
        materialId = this.materialId,
        isDeleted = this.isDeleted,
    )
}