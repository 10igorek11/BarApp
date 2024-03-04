package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto

interface TestRepository {
    suspend fun getTests(): Resource<List<Test>>
    suspend fun getTestsByUser(userId:Int): Resource<List<Test>>
    suspend fun getTest(id: Int): Resource<TestDto>
    suspend fun updateTest(id: Int, test: TestDto): Resource<Any?>
    suspend fun createTest(test: TestDto): Resource<Test>
    suspend fun deleteTest(id: Int): Resource<Any?>
}