package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.CocktailType

interface CocktailTypeRepository {
    suspend fun getCocktailTypes(): Resource<List<CocktailType>>
    suspend fun getDefaultCocktailType(): CocktailType
    suspend fun updateCocktailType(id: Int, cocktailType: CocktailType): Resource<Any?>
    suspend fun createCocktailType(cocktailType: CocktailType): Resource<CocktailType>
    suspend fun deleteCocktailType(id: Int): Resource<Any?>
}