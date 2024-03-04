package com.example.barapp.presentation.screens.tests.test_edit_screen

import android.content.Context
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.QuestionState


sealed class TestEditScreenEvent {
    data class RenameTest(val name:String) : TestEditScreenEvent()
    data class AddQuestion(val question: QuestionState) : TestEditScreenEvent()
    data class UpdateQuestion(val question:QuestionState) : TestEditScreenEvent()
    data class DeleteQuestion(val question:QuestionState) : TestEditScreenEvent()
    data class OnConfirm(val context: Context): TestEditScreenEvent()
    object OnDelete: TestEditScreenEvent()
    data class OnLoad(val testId: Int, val context: Context): TestEditScreenEvent()

}