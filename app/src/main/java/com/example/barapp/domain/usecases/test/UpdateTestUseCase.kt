package com.example.barapp.domain.usecases.test

import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.repositories.TestRepository
import javax.inject.Inject

class UpdateTestUseCase @Inject constructor(
    private val testRepository: TestRepository
) {
    suspend operator fun invoke(test: TestDto) = testRepository.updateTest(test.id, test)
}