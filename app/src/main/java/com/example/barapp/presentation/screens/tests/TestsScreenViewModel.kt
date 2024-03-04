package com.example.barapp.presentation.screens.tests

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.passingstatus.GetPassingStatusesUseCase
import com.example.barapp.domain.usecases.test.GetTestsUseCase
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
class TestsScreenViewModel @Inject constructor(
    private val getTestsUseCase: GetTestsUseCase,
    private val getPassingStatusesUseCase: GetPassingStatusesUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TestsScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestsScreenState())
    private val loadingEventChannel = Channel<LoadingEvent>()
    val loadingEvents = loadingEventChannel.receiveAsFlow()

    init {
        loadTests()
    }
    fun loadTests(){
        _state.update {
            it.copy(
                isLoading = true,
                errorStatus = false
            )
        }
        viewModelScope.launch {
            getLoggedInUserUseCase()?.let{user ->
                when(val tests = getTestsUseCase()){
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

                        when(val passedTests = getPassingStatusesUseCase(user.id)){
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
                                        isLoading = false,
                                        errorStatus = false,
                                        tests = tests.data!!.map{test ->
                                            TestItem(
                                                id = test.id,
                                                name = test.name,
                                                isPassed = passedTests.data!!.any { passingStatus -> passingStatus.testId==test.id }
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
                loadingEventChannel.send(
                    LoadingEvent.SUCCESS
                )
            }

        }
    }

    fun getLoggedInUser() = getLoggedInUserUseCase()
    fun refreshList() {
        viewModelScope.launch {
            loadingEventChannel.send(
                LoadingEvent.DEFAULT
            )
        }
    }

}