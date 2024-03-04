package com.example.barapp.presentation.screens.tests.test_edit_screen

import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.QuestionState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.TestState

data class TestEditScreenState(
    var test: TestState = TestState(),
    var questions: List<QuestionState> = emptyList(),
    var isLoading:Boolean = true,
    var isModified:Boolean = false,
)
