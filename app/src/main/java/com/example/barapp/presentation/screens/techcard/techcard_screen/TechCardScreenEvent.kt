package com.example.barapp.presentation.screens.techcard.techcard_screen

import com.example.barapp.domain.models.Role

sealed class TechCardScreenEvent{
    data class OnLoad(val id:Int): TechCardScreenEvent()
}
