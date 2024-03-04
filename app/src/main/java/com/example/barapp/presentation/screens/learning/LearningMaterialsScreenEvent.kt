package com.example.barapp.presentation.screens.learning

import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.models.Test

sealed class LearningMaterialsScreenEvent{
    object LoadData: LearningMaterialsScreenEvent()
    object Refresh: LearningMaterialsScreenEvent()
}
