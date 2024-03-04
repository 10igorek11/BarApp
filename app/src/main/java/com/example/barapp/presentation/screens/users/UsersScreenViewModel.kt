package com.example.barapp.presentation.screens.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.R
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.UserDto
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.auth.LoginUseCase
import com.example.barapp.domain.usecases.auth.SaveLoggedInUserUseCase
import com.example.barapp.domain.usecases.passingstatus.GetPassingStatusesUseCase
import com.example.barapp.domain.usecases.test.GetTestsByUserUseCase
import com.example.barapp.domain.usecases.users.DeleteUserUseCase
import com.example.barapp.domain.usecases.users.GetUsersUseCase
import com.example.barapp.presentation.screens.auth.LoggingInEvent
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
class UsersScreenViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase,
    private val saveLoggedInUserUseCase: SaveLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(UsersScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UsersScreenState())
    private val deleteEventChannel = Channel<UserDeleteEvent>()
    val deleteEvents = deleteEventChannel.receiveAsFlow()

    init {
        loadUsers()
    }

    private fun loadUsers() {
        _state.update {
            it.copy(
                isLoading = true,
                errorStatus = false
            )
        }
        viewModelScope.launch {
            when (val users:Resource<List<UserDto>> = getUsersUseCase()) {
                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorStatus = true
                        )
                    }
                }
                is Resource.Loading -> return@launch
                is Resource.Success -> {
                    _state.update {
                        it.copy(
                            users = users.data!!,
                            isLoading = false,
                        )
                    }
                }
            }


        }
    }

    fun onEvent(event: UsersScreenEvent) {
        when (event) {
            is UsersScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    deleteUserUseCase(event.userId)
                    if(event.userId == getLoggedInUser()?.id){
                        saveLoggedInUserUseCase(null)
                        deleteEventChannel.send(
                            UserDeleteEvent.CurrentUserDeleted
                        )
                        return@launch
                    }
                    loadUsers()
                }
            }
            UsersScreenEvent.OnLoad -> {
                loadUsers()
            }

            UsersScreenEvent.OnClear -> {
                _state.update {
                    it.copy(
                        isLoading = true,
                        users = emptyList()
                    )
                }
            }
        }
    }

    fun getLoggedInUser() = getLoggedInUserUseCase()

}