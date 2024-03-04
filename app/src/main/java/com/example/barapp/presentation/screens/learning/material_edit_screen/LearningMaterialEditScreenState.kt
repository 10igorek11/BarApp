package com.example.barapp.presentation.screens.learning.material_edit_screen

import com.example.barapp.domain.models.ContentDto
import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.models.QuestionDto
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.models.UserDto

data class LearningMaterialEditScreenState(
    var material: LearningMaterialDto = LearningMaterialDto(),
    var materialError: String? = null,
    var contents: List<ContentDto> = emptyList(),
    var isLoading: Boolean = true,
    var isModified: Boolean = false,
)
