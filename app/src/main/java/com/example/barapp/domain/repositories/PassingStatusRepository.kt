package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.PassingStatus

interface PassingStatusRepository {
    suspend fun getPassingStatuses(userId: Int): Resource<List<PassingStatus>>
    suspend fun createPassingStatus(passingStatus: PassingStatus): Resource<PassingStatus>
}