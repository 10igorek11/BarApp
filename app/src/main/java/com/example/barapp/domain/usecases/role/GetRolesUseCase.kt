package com.example.barapp.domain.usecases.role

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.Role
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.repositories.RoleRepository
import com.example.barapp.domain.repositories.TestRepository
import javax.inject.Inject

class GetRolesUseCase @Inject constructor(
    private val roleRepository: RoleRepository
) {
    suspend operator fun invoke(): Resource<List<Role>> = roleRepository.getRoles()
}