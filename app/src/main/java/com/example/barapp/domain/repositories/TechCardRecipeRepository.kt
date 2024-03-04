package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.data.remote.models.TechCardRecipeDtoResponse
import com.example.barapp.domain.models.TechCardRecipe
import com.example.barapp.domain.models.TechCardRecipeDto

interface TechCardRecipeRepository {
    suspend fun getTechCardRecipes(techCardId: Int): Resource<List<TechCardRecipeDto>>
    suspend fun updateTechCardRecipe(id: Int, techCardRecipeDto: TechCardRecipeDto): Resource<Any?>
    suspend fun createTechCardRecipe(techCardRecipeDto: TechCardRecipeDto): Resource<TechCardRecipe>
    suspend fun deleteTechCardRecipe(id: Int): Resource<Any?>
}