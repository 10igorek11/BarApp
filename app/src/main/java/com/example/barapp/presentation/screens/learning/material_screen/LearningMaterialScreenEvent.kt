package com.example.barapp.presentation.screens.learning.material_screen

import com.example.barapp.domain.models.Role

sealed class LearningMaterialScreenEvent{
    data class OnLoad(val id:Int): LearningMaterialScreenEvent()
}
