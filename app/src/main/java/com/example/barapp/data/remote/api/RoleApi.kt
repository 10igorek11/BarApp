package com.example.barapp.data.remote.api

import com.example.barapp.data.remote.models.RoleResponse
import retrofit2.Response
import retrofit2.http.GET

interface RoleApi {
    @GET(ROLE_URL)
    suspend fun getRoles(): Response<List<RoleResponse>>

    companion object {
        const val ROLE_URL = "roles"
    }
}
