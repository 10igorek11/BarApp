package com.example.barapp.data

import android.content.Context
import com.example.barapp.R
import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.CocktailTypeApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.repositories.CocktailTypeRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import java.net.ConnectException
import javax.inject.Inject

class CocktailTypeRepositoryImpl @Inject constructor(
    private val api: CocktailTypeApi,
    @ApplicationContext private val context: Context,
) : CocktailTypeRepository {
    override suspend fun getCocktailTypes(): Resource<List<CocktailType>> {
        try {
            val response = api.getCocktailTypes()
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

    override suspend fun getDefaultCocktailType(): CocktailType {
        return CocktailType(
            id = 0,
            name = context.getString(R.string.no_chosen)
        )
    }

    override suspend fun updateCocktailType(id: Int, cocktailType: CocktailType): Resource<Any?> {
        try {
            val response = api.updateCocktailType(id, cocktailType.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createCocktailType(cocktailType: CocktailType): Resource<CocktailType> {
        try {
            val response = api.createCocktailType(cocktailType.toResponseModel())
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

    override suspend fun deleteCocktailType(id: Int): Resource<Any?> {
        try {
            val response = api.deleteCocktailType(id)
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }
}