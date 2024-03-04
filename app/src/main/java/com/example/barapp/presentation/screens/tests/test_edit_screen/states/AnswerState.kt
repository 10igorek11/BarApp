package com.example.barapp.presentation.screens.tests.test_edit_screen.states

data class AnswerState(
    var id: Int = 0,
    var text: String = "",
    var textError: String? = null,
    var isCorrect: Boolean = false,
    var questionId: Int = 0,
    var isDeleted: Boolean = false
)
