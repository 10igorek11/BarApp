package com.example.barapp.presentation.screens.users.user

import com.example.barapp.domain.models.Role

sealed class UserScreenEvent{
    data class OnLoad(val id:Int): UserScreenEvent()
    object OnConfirm: UserScreenEvent()
    data class FirstNameChanged(val firstName:String): UserScreenEvent()
    data class LastNameChanged(val lastName:String): UserScreenEvent()
    data class MiddleNameChanged(val middleName:String): UserScreenEvent()
    data class PhoneChanged(val phone:String): UserScreenEvent()
    data class PasswordChanged(val password:String): UserScreenEvent()
    data class RoleChanged(val roleId:Int): UserScreenEvent()
}
