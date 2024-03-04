package com.example.barapp.domain.usecases.techcardrecipe

import com.example.barapp.domain.models.TechCardRecipe
import com.example.barapp.domain.models.TechCardRecipeDto
import com.example.barapp.domain.models.User
import com.example.barapp.domain.repositories.TechCardRecipeRepository
import com.example.barapp.domain.repositories.UserRepository
import javax.inject.Inject

class UpdateTechCardRecipeUseCase @Inject constructor(
    private val techCardRecipeRepository: TechCardRecipeRepository
) {
    suspend operator fun invoke(techCardRecipe: TechCardRecipeDto) = techCardRecipeRepository.updateTechCardRecipe(techCardRecipe.id, techCardRecipe)
}