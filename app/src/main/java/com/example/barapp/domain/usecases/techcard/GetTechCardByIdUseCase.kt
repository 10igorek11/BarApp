package com.example.barapp.domain.usecases.techcard

import com.example.barapp.domain.repositories.TechCardRepository
import javax.inject.Inject

class GetTechCardByIdUseCase @Inject constructor(
    private val techCardRepository: TechCardRepository,
) {
    suspend operator fun invoke(id: Int) = techCardRepository.getTechCard(id)
}
