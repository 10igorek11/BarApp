package com.example.barapp.domain.models

data class TechCard(
    val id: Int = 0,
    val name: String = "",
    val applicationScope: String = "",
    val rawMaterialRequirements: String = "",
    val technologicalProcess: String = "",
    val typeId: Int = 0
)
