package com.example.barapp.data

import android.util.Log
import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.TestApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.repositories.TestRepository
import java.net.ConnectException
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val api: TestApi
) : TestRepository {
    override suspend fun getTests(): Resource<List<Test>> {
        try {
            val response = api.getTests()
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

    override suspend fun getTestsByUser(userId: Int): Resource<List<Test>> {
        try {
            val response = api.getTestsByUser(userId)
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

    override suspend fun getTest(id: Int): Resource<TestDto> {
        try {
            val response = api.getTest(id)
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

    override suspend fun updateTest(id: Int, test: TestDto): Resource<Any?> {
        try {
            val response = api.updateTest(id, test.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createTest(test: TestDto): Resource<Test> {
        try {
            val response = api.createTest(test.toResponseModel())
            if (response.isSuccessful) {
                return Resource.Success(Test())
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun deleteTest(id: Int): Resource<Any?> {
        try {
            val response = api.deleteTest(id)
            if (response.isSuccessful) {
                return Resource.Success(null)
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }
}