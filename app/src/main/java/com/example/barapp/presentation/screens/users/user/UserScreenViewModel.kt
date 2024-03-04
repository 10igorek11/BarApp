package com.example.barapp.presentation.screens.users.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.EMPLOYEE_ROLE
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.User
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.role.GetRolesUseCase
import com.example.barapp.domain.usecases.users.CreateUserUseCase
import com.example.barapp.domain.usecases.users.GetUserByIdUseCase
import com.example.barapp.domain.usecases.users.UpdateUserUseCase
import com.example.barapp.domain.usecases.validation.ValidateFirstNameUseCase
import com.example.barapp.domain.usecases.validation.ValidateLastNameUseCase
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
class UserScreenViewModel @Inject constructor(
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val getRolesUseCase: GetRolesUseCase,
    private val validateFirstNameUseCase: ValidateFirstNameUseCase,
    private val validateLastNameUseCase: ValidateLastNameUseCase,
    private val validatePhoneUseCase: ValidatePhoneUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UserScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UserScreenState())
    private val serverEventChannel = Channel<ServerEvent>()
    val serverEvents = serverEventChannel.receiveAsFlow()

    fun onEvent(event: UserScreenEvent) {
        when (event) {
            is UserScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    when (val roles = getRolesUseCase()) {
                        is Resource.Error -> {
                            serverEventChannel.send(ServerEvent.ERROR)
                        }
                        is Resource.Loading -> return@launch
                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    roles = roles.data!!
                                )
                            }
                        }
                    }
                    if (event.id > 0) {
                        when (val user: Resource<User> = getUserByIdUseCase(event.id)) {
                            is Resource.Error -> {
                                serverEventChannel.send(ServerEvent.ERROR)
                            }
                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        userId = user.data!!.id,
                                        firstName = user.data.firstName,
                                        lastName = user.data.lastName,
                                        middleName = user.data.middleName,
                                        phone = user.data.phone,
                                        password = user.data.password,
                                        roleId = user.data.roleId
                                    )
                                }
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                roleId = EMPLOYEE_ROLE,
                            )
                        }
                    }
                }
            }

            UserScreenEvent.OnConfirm -> {
                val firstNameResult = validateFirstNameUseCase(_state.value.firstName.trim())
                val lastNameResult = validateLastNameUseCase(_state.value.lastName.trim())
                val phoneResult = validatePhoneUseCase(_state.value.phone.trim())
                val passwordResult = validatePasswordUseCase(_state.value.password.trim())
                val hasError = listOf(
                    firstNameResult,
                    lastNameResult,
                    phoneResult,
                    passwordResult
                ).any { !it.successful }
                if (hasError) {
                    _state.update {
                        it.copy(
                            firstNameError = firstNameResult.errorMessage,
                            lastNameError = lastNameResult.errorMessage,
                            phoneError = phoneResult.errorMessage,
                            passwordError = passwordResult.errorMessage
                        )
                    }
                    return
                }
                viewModelScope.launch {
                    if (_state.value.userId == 0) {
                        createUserUseCase(
                            User(
                                id = _state.value.userId,
                                firstName = _state.value.firstName.trim(),
                                lastName = _state.value.lastName.trim(),
                                middleName = if (_state.value.lastName.trim()
                                        .isBlank()
                                ) null else _state.value.middleName,
                                phone = _state.value.phone,
                                password = _state.value.password.trim(),
                                roleId = _state.value.roleId
                            )
                        )
                        serverEventChannel.send(ServerEvent.SUCCESS)
                    }
                    else {
                        updateUserUseCase(
                            User(
                                id = _state.value.userId,
                                firstName = _state.value.firstName.trim(),
                                lastName = _state.value.lastName.trim(),
                                middleName = if (_state.value.lastName.trim()
                                        .isBlank()
                                ) null else _state.value.middleName,
                                phone = _state.value.phone,
                                password = _state.value.password.trim(),
                                roleId = _state.value.roleId
                            )
                        )
                        serverEventChannel.send(ServerEvent.SUCCESS)
                    }
                }
            }

            is UserScreenEvent.FirstNameChanged -> {
                _state.update {
                    it.copy(
                        firstName = event.firstName,
                        firstNameError = null
                    )
                }
            }

            is UserScreenEvent.LastNameChanged -> {
                _state.update {
                    it.copy(
                        lastName = event.lastName,
                        lastNameError = null
                    )
                }
            }

            is UserScreenEvent.MiddleNameChanged -> {
                _state.update {
                    it.copy(
                        middleName = event.middleName,
                        middleNameError = null
                    )
                }
            }

            is UserScreenEvent.PasswordChanged -> {
                _state.update {
                    it.copy(
                        password = event.password,
                        passwordError = null
                    )
                }
            }

            is UserScreenEvent.PhoneChanged -> {
                _state.update {
                    it.copy(
                        phone = event.phone,
                        phoneError = null
                    )
                }
            }

            is UserScreenEvent.RoleChanged -> {
                _state.update {
                    it.copy(
                        roleId = event.roleId
                    )
                }
            }
        }
    }

    fun getLoggedInUser() = getLoggedInUserUseCase()

}