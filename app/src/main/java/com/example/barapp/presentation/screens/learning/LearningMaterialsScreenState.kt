package com.example.barapp.presentation.screens.learning

import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.models.Test

data class LearningMaterialsScreenState(
    var materials:List<LearningMaterial> = emptyList(),
    var errorStatus:Boolean = false,
    var isLoading:Boolean = true
)
