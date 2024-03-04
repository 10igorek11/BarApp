package com.example.barapp.domain.usecases.test

import com.example.barapp.domain.repositories.TestRepository
import javax.inject.Inject

class GetTestsUseCase @Inject constructor(
    private val testRepository: TestRepository
) {
    suspend operator fun invoke() = testRepository.getTests()
}