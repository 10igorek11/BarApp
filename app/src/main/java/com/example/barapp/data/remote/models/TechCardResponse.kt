package com.example.barapp.data.remote.models

data class TechCardResponse(
    val id: Int,
    val name: String,
    val applicationScope: String,
    val rawMaterialRequirements: String,
    val technologicalProcess: String,
    val typeId: Int
)
