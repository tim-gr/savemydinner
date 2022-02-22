package com.tgad.savemydinner.data.api.dtos

import com.squareup.moshi.JsonClass
import com.tgad.savemydinner.domain.entities.Ingredient

@JsonClass(generateAdapter = true)
data class IngredientDto(
    val name: String
)

fun List<IngredientDto>.asDomainModel(): List<Ingredient> {
    return this.map {
        Ingredient(
            name = it.name
        )
    }
}