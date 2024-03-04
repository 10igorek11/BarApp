package com.example.barapp.domain.usecases.cocktailtype

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.CocktailType
import com.example.barapp.domain.repositories.CocktailTypeRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class GetCocktailTypesUseCase @Inject constructor(
    private val cocktailTypeRepository: CocktailTypeRepository
) {
    suspend operator fun invoke(): Resource<List<CocktailType>> = cocktailTypeRepository.getCocktailTypes()
}