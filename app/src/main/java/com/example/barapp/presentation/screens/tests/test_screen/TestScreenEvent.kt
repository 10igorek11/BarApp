package com.example.barapp.presentation.screens.tests.test_screen

import com.example.barapp.domain.models.AnswerDto

sealed class TestScreenEvent {
    data class AnswerChanged(val questionId:Int, val answer: AnswerDto) : TestScreenEvent()
    data class OnConfirm(val passedQuestions: Int): TestScreenEvent()
    data class OnLoad(val testId: Int): TestScreenEvent()

}