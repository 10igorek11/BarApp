package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.AnswerDtoResponse
import com.example.barapp.data.remote.models.PhotoDataResponse
import com.example.barapp.data.remote.models.PhotoResponse
import com.example.barapp.domain.models.AnswerDto
import com.example.barapp.domain.models.Photo
import com.example.barapp.domain.models.PhotoData

fun PhotoResponse.toModel(): Photo {
    return Photo(
        data = this.data.toModel()
    )
}
fun Photo.toResponseModel(): PhotoResponse {
    return PhotoResponse(
        data = this.data.toResponseModel()
    )
}
fun PhotoDataResponse.toModel(): PhotoData {
    return PhotoData(
        id = this.id,
        title = this.title,
        url = this.url
    )
}
fun PhotoData.toResponseModel(): PhotoDataResponse {
    return PhotoDataResponse(
        id = this.id,
        title = this.title,
        url = this.url
    )
}
