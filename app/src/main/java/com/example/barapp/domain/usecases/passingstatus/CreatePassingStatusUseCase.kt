package com.example.barapp.domain.usecases.passingstatus

import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.models.PassingStatus
import com.example.barapp.domain.repositories.CocktailTypeRepository
import com.example.barapp.domain.repositories.PassingStatusRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class CreatePassingStatusUseCase @Inject constructor(
    private val passingStatusRepository: PassingStatusRepository
) {
    suspend operator fun invoke(passingStatus: PassingStatus) = passingStatusRepository.createPassingStatus(passingStatus)
}