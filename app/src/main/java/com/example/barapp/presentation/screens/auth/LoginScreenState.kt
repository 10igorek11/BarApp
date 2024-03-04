package com.example.barapp.presentation.screens.auth

data class LoginScreenState(
    var phone:String = "",
    var phoneError:String? =null,
    var password:String = "",
    var passwordError:String?=null,
    val isLoading: Boolean = false
)
