package com.example.barapp.domain.usecases.cocktailtype

import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.repositories.CocktailTypeRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class DeleteCocktailTypeUseCase @Inject constructor(
    private val cocktailTypeRepository: CocktailTypeRepository
) {
    suspend operator fun invoke(id: Int) = cocktailTypeRepository.deleteCocktailType(id)
}