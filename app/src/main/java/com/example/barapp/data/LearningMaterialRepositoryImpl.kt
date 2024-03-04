package com.example.barapp.data

import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.LearningMaterialApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.LearningMaterial
import com.example.barapp.domain.models.LearningMaterialDto
import com.example.barapp.domain.repositories.LearningMaterialRepository
import java.net.ConnectException
import javax.inject.Inject

class LearningMaterialRepositoryImpl @Inject constructor(
    private val api: LearningMaterialApi
) : LearningMaterialRepository {
    override suspend fun getLearningMaterials(): Resource<List<LearningMaterial>> {
        try {
            val response = api.getLearningMaterials()
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse.map { it.toModel() })
                }
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun getLearningMaterial(id: Int): Resource<LearningMaterialDto> {
        try {
            val response = api.getLearningMaterial(id)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse.toModel())
                }
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun updateLearningMaterial(id: Int, learningMaterial: LearningMaterialDto): Resource<Any?> {
        try {
            val response = api.updateLearningMaterial(id, learningMaterial.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createLearningMaterial(learningMaterial: LearningMaterialDto): Resource<LearningMaterial> {
        try {
            val response = api.createLearningMaterial(learningMaterial.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(LearningMaterial())

            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun deleteLearningMaterial(id: Int): Resource<Any?> {
        try {
            val response = api.deleteLearningMaterial(id)
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }
}