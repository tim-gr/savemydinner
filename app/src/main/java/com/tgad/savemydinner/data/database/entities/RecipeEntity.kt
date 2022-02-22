package com.tgad.savemydinner.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tgad.savemydinner.domain.entities.Recipe

@Entity
data class RecipeEntity constructor(
    @PrimaryKey
    val recipeId: Long,
    val title: String,
    val imageUrl: String
)

fun List<RecipeEntity>.asDomainModel(): List<Recipe> {
    return map {
        Recipe(
            recipeId = it.recipeId,
            title = it.title,
            imageUrl = it.imageUrl
        )
    }
}