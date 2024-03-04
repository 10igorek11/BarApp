package com.example.barapp.domain.usecases.techcard

import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto
import com.example.barapp.domain.models.Test
import com.example.barapp.domain.models.TestDto
import com.example.barapp.domain.repositories.TechCardRepository
import com.example.barapp.domain.repositories.TestRepository
import javax.inject.Inject

class GetTechCardsUseCase @Inject constructor(
    private val techCardRepository: TechCardRepository
) {
    suspend operator fun invoke(searchText:String, typeId:Int) = techCardRepository.getTechCards(searchText, typeId)
}