package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.PassingStatusResponse
import com.example.barapp.domain.models.PassingStatus

fun PassingStatusResponse.toModel(): PassingStatus {
    return PassingStatus(
        id = this.id,
        userId = this.userId,
        testId = this.testId,
    )
}

fun PassingStatus.toResponseModel(): PassingStatusResponse {
    return PassingStatusResponse(
        id = this.id,
        userId = this.userId,
        testId = this.testId,
    )
}
