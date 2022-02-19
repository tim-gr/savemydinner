package com.tgad.savemydinner.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.tgad.savemydinner.domain.Recipe

@Entity
data class DatabaseRecipe constructor(
    @PrimaryKey
    val recipeId: Long,
    val title: String
)

fun List<DatabaseRecipe>.asDomainModel(): List<Recipe> {
    return map {
        Recipe(
            recipeId = it.recipeId,
            title = it.title
        )
    }
}