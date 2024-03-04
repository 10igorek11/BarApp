package com.example.barapp.presentation.screens.tests.test_edit_screen.states.mappers

import com.example.barapp.domain.models.AnswerDto
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.TestDto
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.AnswerState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.QuestionState
import com.example.barapp.presentation.screens.tests.test_edit_screen.states.TestState

fun AnswerState.toDto():AnswerDto{
    return AnswerDto(
        id = this.id,
        text = this.text,
        isCorrect = this.isCorrect,
        isDeleted = this.isDeleted,
        questionId = this.questionId
    )
}
fun AnswerDto.toState():AnswerState{
    return AnswerState(
        id = this.id,
        text = this.text,
        isCorrect = this.isCorrect,
        isDeleted = this.isDeleted,
        questionId = this.questionId
    )
}
fun QuestionState.toDto(): QuestionDto {
    return QuestionDto(
        id = this.id,
        text = this.text,
        photo = this.photo,
        isDeleted = this.isDeleted,
        testId = this.testId,
        answers = this.answers.map { it.toDto() }
    )
}
fun QuestionDto.toState(): QuestionState {
    return QuestionState(
        id = this.id,
        text = this.text,
        photo = this.photo,
        isDeleted = this.isDeleted,
        testId = this.testId,
        answers = this.answers.map { it.toState() }
    )
}
fun TestState.toDto(): TestDto {
    return TestDto(
        id = this.id,
        name = this.name,
        questions = this.questions.map { it.toDto() }
    )
}
fun TestDto.toState(): TestState {
    return TestState(
        id = this.id,
        name = this.name,
        questions = this.questions.map { it.toState() }
    )
}
