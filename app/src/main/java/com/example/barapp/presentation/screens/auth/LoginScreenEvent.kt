package com.example.barapp.presentation.screens.auth

import android.content.Context

sealed class LoginScreenEvent {
    data class PhoneNumberChanged(val phone:String) : LoginScreenEvent()
    data class PasswordChanged(val password:String) : LoginScreenEvent()
    data class OnConfirm(val context: Context): LoginScreenEvent()

}