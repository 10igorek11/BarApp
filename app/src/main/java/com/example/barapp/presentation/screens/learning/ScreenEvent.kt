package com.example.barapp.presentation.screens.learning

sealed class ScreenEvent{
    object SUCCESS: ScreenEvent()
    object DEFAULT: ScreenEvent()
    object ERROR: ScreenEvent()
}
