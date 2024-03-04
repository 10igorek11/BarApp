package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.RoleResponse
import com.example.barapp.domain.models.Role

fun RoleResponse.toModel(): Role {
    return Role(
        id = this.id,
        name = this.name,
    )
}

fun Role.toResponseModel(): RoleResponse {
    return RoleResponse(
        id = this.id,
        name = this.name,
    )
}