package com.example.barapp.domain.usecases.learningmaterial

import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.LearningMaterialRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateLearningMaterialUseCase @Inject constructor(
    private val learningMaterialRepository: LearningMaterialRepository
) {
    suspend operator fun invoke(learningMaterial: LearningMaterialDto) = learningMaterialRepository.updateLearningMaterial(learningMaterial.id, learningMaterial)
}