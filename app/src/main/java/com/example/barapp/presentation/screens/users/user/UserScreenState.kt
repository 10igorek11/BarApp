package com.example.barapp.presentation.screens.users.user

import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.models.User

data class UserScreenState(
    var userId: Int = 0,
    var firstName: String = "",
    var firstNameError: String? = null,
    var lastName: String = "",
    var lastNameError: String? = null,
    var middleName: String? = null,
    var middleNameError: String? = null,
    var phone: String = "",
    var phoneError: String? = null,
    var password: String = "",
    var passwordError: String? = null,
    var roleId: Int = 0,
    var roles: List<Role> = emptyList()
)
