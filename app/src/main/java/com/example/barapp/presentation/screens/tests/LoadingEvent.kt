package com.example.barapp.presentation.screens.tests

sealed class LoadingEvent{
    object SUCCESS: LoadingEvent()
    object DEFAULT: LoadingEvent()
}
