package com.example.barapp.presentation.screens.tests.test_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.passingstatus.CreatePassingStatusUseCase
import com.example.barapp.domain.usecases.test.GetTestByIdUseCase
import com.example.barapp.presentation.screens.tests.LoadingEvent
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
class TestScreenViewModel @Inject constructor(
    private val getTestByIdUseCase: GetTestByIdUseCase,
    private val createPassingStatusUseCase: CreatePassingStatusUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TestScreenState())
    var state = _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestScreenState())
    val list = mutableStateListOf<List<Boolean>>()
    private val loadingEventChannel = Channel<LoadingEvent>()
    val loadingEvents = loadingEventChannel.receiveAsFlow()

    fun onEvent(event:TestScreenEvent){
        when(event){
            is TestScreenEvent.AnswerChanged -> {
                _state.update {
                    it.copy(
                        userAnswers = it.userAnswers.map { question ->
                            if(question.id == event.questionId){
                                question.apply {
                                    answers = question.answers.map{answer->
                                        if(answer.id == event.answer.id) event.answer else answer
                                    }
                                }
                            }else{
                                question
                            }
                        }
                    )
                }
            }
            is TestScreenEvent.OnConfirm -> {
                _state.update {
                    it.copy(
                        confirmed = true,
                        passedQuestions = event.passedQuestions
                    )
                }
                if(event.passedQuestions == _state.value.test.questions.size){
                    val userId = getLoggedInUserUseCase()?.id
                    userId?.let{
                        viewModelScope.launch {
                            createPassingStatusUseCase(
                                PassingStatus(
                                    userId = it,
                                    testId = _state.value.test.id
                                )
                            )
                        }
                    }
                }

            }
            is TestScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    if(_state.value.test.questions.isEmpty()){
                        when(val result = getTestByIdUseCase(event.testId)){
                            is Resource.Error -> {

                            }
                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        test = result.data!!
                                            .copy(
                                            questions = result.data.questions   .map { q->
                                                q.copy(
                                                    answers = q.answers.shuffled()
                                                )
                                            }
                                        )

                                    )
                                }
                            }
                        }
                    }
                    loadingEventChannel.send(LoadingEvent.SUCCESS)
                }
            }
        }
    }

}
