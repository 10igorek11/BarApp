package com.example.barapp.presentation.screens.tests.test_edit_screen.states

data class TestState(
    var id: Int = 0,
    var name: String = "",
    var testError: String?=null,
    var questions: List<QuestionState> = emptyList()
)
