package com.example.barapp.data

import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.TechCardApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.domain.repositories.TechCardRepository
import java.net.ConnectException
import javax.inject.Inject

class TechCardRepositoryImpl @Inject constructor(
    private val api: TechCardApi
) : TechCardRepository {
    override suspend fun getTechCards(searchText:String, typeId:Int): Resource<List<TechCardDto>> {
        try {
            val response = api.getTechCards(searchText, typeId)
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

    override suspend fun getTechCard(id: Int): Resource<TechCard> {
        try {
            val response = api.getTechCard(id)
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

    override suspend fun updateTechCard(id: Int, techCard: TechCard): Resource<Any?> {
        try {
            val response = api.updateTechCard(id, techCard.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createTechCard(techCard: TechCard): Resource<TechCard> {
        try {
            val response = api.createTechCard(techCard.toResponseModel())
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

    override suspend fun deleteTechCard(id: Int): Resource<Any?> {
        try {
            val response = api.deleteTechCard(id)
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }
}