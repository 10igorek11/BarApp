package com.example.barapp.presentation.screens.home

import androidx.lifecycle.ViewModel
import com.example.barapp.domain.models.User
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.auth.SaveLoggedInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val saveLoggedInUserUseCase: SaveLoggedInUserUseCase,
) : ViewModel() {
    fun getUserRole() = getLoggedInUserUseCase()?.roleId ?: 1
    fun saveUser(user: User?) {
        saveLoggedInUserUseCase(user)
    }
}
