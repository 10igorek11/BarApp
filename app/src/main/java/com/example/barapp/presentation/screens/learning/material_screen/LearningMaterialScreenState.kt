package com.example.barapp.presentation.screens.learning.material_screen

import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.models.UserDto

data class LearningMaterialScreenState(
    var material: LearningMaterialDto = LearningMaterialDto(),
    var isLoading:Boolean = true,
)
