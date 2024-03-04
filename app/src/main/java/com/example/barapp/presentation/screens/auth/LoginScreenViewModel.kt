package com.example.barapp.presentation.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.R
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.usecases.auth.LoginUseCase
import com.example.barapp.domain.usecases.auth.SaveLoggedInUserUseCase
import com.example.barapp.domain.usecases.users.GetUsersUseCase
import com.example.barapp.domain.usecases.validation.ValidatePasswordUseCase
import com.example.barapp.domain.usecases.validation.ValidatePhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveLoggedInUserUseCase: SaveLoggedInUserUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), LoginScreenState())
    private val loggingInEventChannel = Channel<LoggingInEvent>()
    val loggingInEvents = loggingInEventChannel.receiveAsFlow()

    fun onEvent(event:LoginScreenEvent){
        when (event){
            is LoginScreenEvent.PhoneNumberChanged -> {
                _state.update {
                    it.copy(
                        phone = event.phone,
                        phoneError = null
                    )
                }
            }
            is LoginScreenEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.password,
                        passwordError = null
                    )
                }
            }
            is LoginScreenEvent.OnConfirm -> {
                _state.update {
                    it.copy(
                        isLoading = true
                    )
                }
                val phoneResult = validatePhoneUseCase(_state.value.phone)
                val passwordResult = validatePasswordUseCase(_state.value.password)
                if(!phoneResult.successful || !passwordResult.successful){
                    _state.update {
                        it.copy(
                            passwordError = passwordResult.errorMessage,
                            phoneError = phoneResult.errorMessage,
                            isLoading = false
                        )
                    }
                    return
                }
                viewModelScope.launch {
                    when (val result = loginUseCase(state.value.phone.trim(), state.value.password.trim())){
                        is Resource.Error -> {
                            when (result.message) {
                                "login" -> {
                                    _state.update {
                                        it.copy(
                                            phoneError = event.context.getString(R.string.user_not_found_error),
                                            isLoading = false
                                        )
                                    }
                                }
                                "password" -> {
                                    _state.update {
                                        it.copy(
                                            passwordError = event.context.getString(R.string.password_not_correct_error),
                                            isLoading = false
                                        )
                                    }
                                }
                                else -> {
                                    _state.update {
                                        it.copy(
                                            isLoading = false
                                        )
                                    }
                                }
                            }
                        }
                        is Resource.Loading -> {
                            _state.update {
                                it.copy(
                                    isLoading = true
                                )
                            }
                        }
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    isLoading = false
                                )
                            }
                            saveLoggedInUserUseCase(result.data)
                            loggingInEventChannel.send(LoggingInEvent.SUCCESS)
                        }
                    }
                }
            }
        }
    }

}