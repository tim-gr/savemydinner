package com.tgad.savemydinner.data.api.dtos

import com.squareup.moshi.JsonClass
import com.tgad.savemydinner.data.database.entities.RecipeEntity

@JsonClass(generateAdapter = true)
data class RecipeDto(
    val id: Long,
    val title: String,
    val image: String
)

fun List<RecipeDto>.asDatabaseModel(): Array<RecipeEntity> {
    return this.map {
        RecipeEntity(
            recipeId = it.id,
            title = it.title,
            imageUrl = it.image
        )
    }.toTypedArray()
}