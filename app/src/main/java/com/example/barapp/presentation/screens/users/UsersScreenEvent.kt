package com.example.barapp.presentation.screens.users

import android.content.Context
import com.example.barapp.domain.models.AnswerDto

sealed class UsersScreenEvent {
    data class OnDelete(val userId: Int): UsersScreenEvent()
    object OnLoad: UsersScreenEvent()
    object OnClear: UsersScreenEvent()

}