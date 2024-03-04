package com.example.barapp.data

import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.PassingStatusApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.repositories.PassingStatusRepository
import java.net.ConnectException
import javax.inject.Inject

class PassingStatusRepositoryImpl @Inject constructor(
    private val api: PassingStatusApi
) : PassingStatusRepository {
    override suspend fun getPassingStatuses(userId: Int): Resource<List<PassingStatus>> {
        try {
            val response = api.getPassingStatuses(userId)
            if (response.isSuccessful) {
                response.body()?.let { resultResponse ->
                    return Resource.Success(resultResponse.map{it.toModel()})
                }
            }
            return Resource.Error(response.errorBody().toString())
        } catch (ex: ConnectException) {
            return Resource.Error(SERVER_ERROR);
        }
    }

    override suspend fun createPassingStatus(passingStatus: PassingStatus): Resource<PassingStatus> {
        try {
            val response = api.createPassingStatus(passingStatus.toResponseModel())
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
}