package com.example.barapp.domain.usecases.techcard

import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.repositories.TechCardRepository
import com.example.barapp.domain.repositories.TestRepository
import javax.inject.Inject

class UpdateTechCardUseCase @Inject constructor(
    private val techCardRepository: TechCardRepository
) {
    suspend operator fun invoke(techCard: TechCard) = techCardRepository.updateTechCard(techCard.id, techCard)
}