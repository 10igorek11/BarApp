package com.example.barapp.presentation.screens.tests.test_screen

import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto

data class TestScreenState(
    var test: TestDto = TestDto(),
    var userAnswers:List<QuestionDto> = emptyList(),
    var isLoading:Boolean = true,
    var confirmed:Boolean = false,
    var passedQuestions:Int = -1
)
