package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.Role

interface RoleRepository {
    suspend fun getRoles(): Resource<List<Role>>
}