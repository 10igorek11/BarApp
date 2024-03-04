package com.example.barapp.presentation.screens.users

import com.example.barapp.domain.models.UserDto

data class UsersScreenState(
    var users:List<UserDto> = emptyList(),
    var errorStatus:Boolean = false,
    var isLoading:Boolean = true
)
