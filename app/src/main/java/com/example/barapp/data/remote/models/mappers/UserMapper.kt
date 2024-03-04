package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.UserDtoResponse
import com.example.barapp.data.remote.models.UserResponse
import com.example.barapp.domain.models.User
import com.example.barapp.domain.models.UserDto

fun UserResponse.toModel(): User {
    return User(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        phone = this.phone,
        password = this.password,
        roleId = this.roleId,
    )
}

fun User.toResponseModel(): UserResponse {
    return UserResponse(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        phone = this.phone,
        password = this.password,
        roleId = this.roleId,
    )
}
fun UserDtoResponse.toModel(): UserDto {
    return UserDto(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        roleId = this.roleId,
        passedTests = this.passedTests.map { it.toModel() }
    )
}

fun UserDto.toResponseModel(): UserDtoResponse {
    return UserDtoResponse(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        middleName = this.middleName,
        roleId = this.roleId,
        passedTests = this.passedTests.map { it.toResponseModel() }
    )
}
