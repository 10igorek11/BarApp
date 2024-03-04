package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto

interface LearningMaterialRepository {
    suspend fun getLearningMaterials(): Resource<List<LearningMaterial>>
    suspend fun getLearningMaterial(id: Int): Resource<LearningMaterialDto>
    suspend fun updateLearningMaterial(id: Int, learningMaterial: LearningMaterialDto): Resource<Any?>
    suspend fun createLearningMaterial(learningMaterial: LearningMaterialDto): Resource<LearningMaterial>
    suspend fun deleteLearningMaterial(id: Int): Resource<Any?>
}