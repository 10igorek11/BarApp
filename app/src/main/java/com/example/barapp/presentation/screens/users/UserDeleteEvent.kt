package com.example.barapp.presentation.screens.users

sealed class UserDeleteEvent{
    object CurrentUserDeleted: UserDeleteEvent()
}
