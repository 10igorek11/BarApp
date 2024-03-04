package com.example.barapp.presentation.screens.tests.test_edit_screen.states

data class QuestionState(
    var id: Int = 0,
    var text: String = "",
    var textError: String? = null,
    var answerError: String? = null,
    var photo: String? = null,
    var testId: Int = 0,
    var isDeleted: Boolean = false,
    var answers: List<AnswerState> = emptyList()
)
