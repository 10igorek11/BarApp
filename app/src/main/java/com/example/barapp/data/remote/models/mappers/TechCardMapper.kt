package com.example.barapp.data.remote.models.mappers

import com.example.barapp.data.remote.models.TechCardDtoResponse
import com.example.barapp.data.remote.models.TechCardResponse
import com.example.barapp.domain.models.TechCard
import com.example.barapp.domain.models.TechCardDto

fun TechCardDtoResponse.toModel(): TechCardDto {
    return TechCardDto(
        id = this.id,
        name = this.name,
        typeId = this.typeId,
    )
}

fun TechCardDto.toResponseModel(): TechCardDtoResponse {
    return TechCardDtoResponse(
        id = this.id,
        name = this.name,
        typeId = this.typeId,
    )
}
fun TechCardResponse.toModel(): TechCard {
    return TechCard(
        id = this.id,
        name = this.name,
        applicationScope = this.applicationScope,
        rawMaterialRequirements = this.rawMaterialRequirements,
        technologicalProcess = this.technologicalProcess,
        typeId = this.typeId,
    )
}

fun TechCard.toResponseModel(): TechCardResponse {
    return TechCardResponse(
        id = this.id,
        name = this.name,
        applicationScope = this.applicationScope,
        rawMaterialRequirements = this.rawMaterialRequirements,
        technologicalProcess = this.technologicalProcess,
        typeId = this.typeId,
    )
}

