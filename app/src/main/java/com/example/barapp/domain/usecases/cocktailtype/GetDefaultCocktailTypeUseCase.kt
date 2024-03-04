package com.example.barapp.domain.usecases.cocktailtype

import com.example.barapp.domain.repositories.CocktailTypeRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class GetDefaultCocktailTypeUseCase @Inject constructor(
    private val cocktailTypeRepository: CocktailTypeRepository
) {
    suspend operator fun invoke() = cocktailTypeRepository.getDefaultCocktailType()
}