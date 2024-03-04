package com.example.barapp.domain.usecases.learningmaterial

import com.example.barapp.domain.repositories.LearningMaterialRepository
import javax.inject.Inject

class DeleteLearningMaterialUseCase @Inject constructor(
    private val learningMaterialRepository: LearningMaterialRepository,
) {
    suspend operator fun invoke(id: Int) = learningMaterialRepository.deleteLearningMaterial(id)
}
