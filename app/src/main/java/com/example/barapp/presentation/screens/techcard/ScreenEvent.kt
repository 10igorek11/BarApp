package com.example.barapp.presentation.screens.techcard

sealed class ScreenEvent{
    object SUCCESS: ScreenEvent()
    object DEFAULT: ScreenEvent()
    object ERROR: ScreenEvent()
}
