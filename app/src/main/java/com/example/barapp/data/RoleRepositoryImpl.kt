package com.example.barapp.data

import com.example.barapp.common.SERVER_ERROR
import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.api.RoleApi
import com.example.barapp.data.remote.models.mappers.toModel
import com.example.barapp.data.remote.models.mappers.toResponseModel
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.repositories.RoleRepository
import java.net.ConnectException
import javax.inject.Inject

class RoleRepositoryImpl @Inject constructor(
    private val api: RoleApi
) : RoleRepository {
    override suspend fun getRoles(): Resource<List<Role>> {
        try {
            val response = api.getRoles()
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
}