package com.example.barapp.presentation.screens.tests.test_edit_screen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barapp.R
import com.example.barapp.common.util.Resource
import com.example.barapp.domain.usecases.SetPhotoUseCase
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.domain.usecases.test.CreateTestUseCase
import com.example.barapp.domain.usecases.test.DeleteTestUseCase
import com.example.barapp.domain.usecases.test.GetTestByIdUseCase
import com.example.barapp.domain.usecases.test.UpdateTestUseCase
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.AnswerState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.QuestionState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.TestState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.mappers.toDto
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.mappers.toState
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
class TestEditScreenViewModel @Inject constructor(
    private val getTestByIdUseCase: GetTestByIdUseCase,
    private val createTestUseCase: CreateTestUseCase,
    private val updateTestUseCase: UpdateTestUseCase,
    private val deleteTestUseCase: DeleteTestUseCase,
    private val setPhotoUseCase: SetPhotoUseCase,
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(TestEditScreenState())
    var state =
        _state.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), TestEditScreenState())
    val list = mutableStateListOf<List<Boolean>>()
    private val screenEventChannel = Channel<ScreenEvent>()
    val screenEvents = screenEventChannel.receiveAsFlow()

    fun onEvent(event: TestEditScreenEvent) {
        when (event) {
            is TestEditScreenEvent.AddQuestion -> {
                _state.update {
                    it.copy(
                        test = it.test.copy(
                            testError = null
                        ),
                        questions = it.questions.plus(event.question),
                        isModified = true
                    )
                }
            }

            is TestEditScreenEvent.DeleteQuestion -> {
                _state.update {
                    it.copy(
                        questions = it.questions.minus(event.question),
                        isModified = true
                    )
                }
            }

            is TestEditScreenEvent.OnConfirm -> {
                _state.update {
                    it.copy(
                        questions = it.questions.map { q ->
                            q.copy(
                                textError = if (q.text.trim()
                                        .isBlank()
                                ) event.context.getString(R.string.required_field_error) else null,
                                answerError = if (q.answers.any { a -> a.isCorrect }) null else event.context.getString(
                                    R.string.no_choosen_correct_answer
                                ),
                                answers = q.answers.map { a ->
                                    if (a.text.trim()
                                            .isBlank()
                                    ) a.copy(textError = event.context.getString(R.string.required_field_error)) else a.copy(
                                        textError = null
                                    )
                                }
                            )
                        },
                        test = it.test.copy(
                            testError = if(it.questions.isEmpty()) event.context.getString(R.string.no_questions_added) else null
                        )
                    )
                }
                if(_state.value.test.testError!=null){
                    return
                }
                _state.value.questions.forEach {
                    if (it.textError != null || it.answerError != null || it.answers.none { a -> a.isCorrect } || it.answers.any { a -> a.textError != null }) {
                        return
                    }
                }
                viewModelScope.launch {
                    if(_state.value.test.id==0){
                        val result = createTestUseCase(_state.value.test.copy(
                            questions = _state.value.questions.map {
                                val photo = it.photo
                                if(photo == null){
                                    it
                                }
                                else {
                                    when(val result = setPhotoUseCase(photo)){
                                        is Resource.Error -> {
                                            it.copy(
                                                photo = null
                                            )
                                        }
                                        is Resource.Loading -> {
                                            it.copy(
                                                photo = null
                                            )
                                        }
                                        is Resource.Success -> {
                                            it.copy(
                                                photo = result.data!!.data.url
                                            )
                                        }
                                    }
                                }
                            }
                        ).toDto())
                        when(result){
                            is Resource.Error -> {
                                return@launch
                            }
                            is Resource.Loading -> {
                                return@launch
                            }
                            is Resource.Success -> {
                            }
                        }
                    }
                    else{
                        val mergedQuestions = mutableListOf<QuestionState>()
                        mergedQuestions.addAll(_state.value.test.questions.map{
                            if(_state.value.questions.find { q->q.id == it.id }==null) it.copy(
                                isDeleted = true
                            ) else it
                        })
                        _state.value.questions.forEach {
                            println(it)
                            if(it.id<1){
                                mergedQuestions.add(it)
                            }
                            else{
                                if(!it.isDeleted){
                                    val index = mergedQuestions.indexOf(mergedQuestions.find {q->q.id == it.id })
                                    val mergedAnswers = mutableListOf<AnswerState>()
                                    mergedAnswers.addAll(mergedQuestions[index].answers.map { ma ->
                                        it.answers.find { sa -> sa.id == ma.id } ?: ma.copy(
                                            isDeleted = true
                                        )
                                    })
                                    mergedAnswers.addAll(
                                        it.answers.filter {sa-> sa.id<1  }
                                    )
                                    mergedQuestions[index] = it.copy(
                                        answers = mergedAnswers
                                    )
                                }
                            }
                        }
                        when(updateTestUseCase(_state.value.test.copy(
                            questions = mergedQuestions.map {
                                val photo = it.photo
                                if(photo == null || photo.take(7)=="https:/"){
                                    it
                                }
                                else {
                                    when(val result = setPhotoUseCase(photo)){
                                        is Resource.Error -> {
                                            it.copy(
                                                photo = null
                                            )
                                        }
                                        is Resource.Loading -> {
                                            it.copy(
                                                photo = null
                                            )
                                        }
                                        is Resource.Success -> {
                                            it.copy(
                                                photo = result.data!!.data.url
                                            )
                                        }
                                    }
                                }
                            }
                        ).toDto())){
                            is Resource.Error -> {
                                return@launch
                            }
                            is Resource.Loading -> {
                                return@launch
                            }
                            is Resource.Success -> {

                            }
                        }
                    }
                    screenEventChannel.send(
                        ScreenEvent.SAVED
                    )
                }
            }

            TestEditScreenEvent.OnDelete -> {
                viewModelScope.launch {
                    if(_state.value.test.id>0){
                        deleteTestUseCase(_state.value.test.id)
                    }
                    screenEventChannel.send(
                        ScreenEvent.DELETED
                    )
                }
            }

            is TestEditScreenEvent.OnLoad -> {
                viewModelScope.launch {
                    if (event.testId <= 0) {
                        val questions = listOf(
                            QuestionState(
                                testId = state.value.test.id,
                                answers = listOf(
                                    AnswerState(
                                        text = event.context.getString(R.string.answer, 1),
                                        questionId = 0
                                    )
                                )
                            )
                        )
                        _state.update {
                            it.copy(
                                test = TestState(
                                    name = event.context.getString(R.string.new_test_lbl),
                                ),
                                questions = questions,
                                isModified = true
                            )
                        }
                        screenEventChannel.send(ScreenEvent.LOADED)
                    } else {
                        when (val result = getTestByIdUseCase(event.testId)) {
                            is Resource.Error -> {

                            }

                            is Resource.Loading -> return@launch
                            is Resource.Success -> {
                                _state.update {
                                    it.copy(
                                        test = result.data!!.toState(),
                                        questions = result.data.questions.map { q -> q.toState() }
                                    )
                                }
                            }
                        }
                        screenEventChannel.send(ScreenEvent.LOADED)
                    }
                }
            }

            is TestEditScreenEvent.RenameTest -> {
                _state.update {
                    it.copy(
                        test = it.test.copy(
                            name = event.name
                        ),
                        isModified = true
                    )
                }
            }

            is TestEditScreenEvent.UpdateQuestion -> {
                _state.update {
                    it.copy(
                        questions = it.questions.map {
                                q -> if (q.id == event.question.id) event.question else q
                                                     },
                        isModified = true
                    )
                }
            }
        }
    }

}

sealed class ScreenEvent {
    object LOADED : ScreenEvent()
    object SAVED : ScreenEvent()
    object DELETED : ScreenEvent()
}
