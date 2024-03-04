package com.example.barapp.data

import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.TechCardRecipeApi
import com.example.barapp.data.remote.models.TechCardRecipeDtoResponse
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.TechCardRecipe
import com.example.barapp.domain.models.TechCardRecipeDto
import com.example.barapp.domain.repositories.TechCardRecipeRepository
import java.net.ConnectException
import javax.inject.Inject

class TechCardRecipeRepositoryImpl @Inject constructor(
    private val api: TechCardRecipeApi
) : TechCardRecipeRepository {
    override suspend fun getTechCardRecipes(techCardId:Int): Resource<List<TechCardRecipeDto>> {
        try {
            val response = api.getTechCardRecipes(techCardId)
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

    override suspend fun updateTechCardRecipe(id: Int, techCard: TechCardRecipeDto): Resource<Any?> {
        try {
            val response = api.updateTechCardRecipe(id, techCard.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createTechCardRecipe(techCard: TechCardRecipeDto): Resource<TechCardRecipe> {
        try {
            val response = api.createTechCardRecipe(techCard.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(TechCardRecipe())
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun deleteTechCardRecipe(id: Int): Resource<Any?> {
        try {
            val response = api.deleteTechCardRecipe(id)
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }
}