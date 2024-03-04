package com.example.barapp.domain.repositories

import com.example.barapp.common.util.Resource
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto

interface TechCardRepository {
    suspend fun getTechCards(searchText:String, typeId:Int): Resource<List<TechCardDto>>
    suspend fun getTechCard(id: Int): Resource<TechCard>
    suspend fun updateTechCard(id: Int, techCard: TechCard): Resource<Any?>
    suspend fun createTechCard(techCard: TechCard): Resource<TechCard>
    suspend fun deleteTechCard(id: Int): Resource<Any?>
}