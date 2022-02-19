package com.tgad.savemydinner.network

import com.squareup.moshi.JsonClass
import com.tgad.savemydinner.database.DatabaseRecipe

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    val id: Long,
    val title: String,
    val image: String
)

fun List<NetworkRecipe>.asDatabaseModel(): Array<DatabaseRecipe> {
    return this.map {
        DatabaseRecipe(
            recipeId = it.id,
            title = it.title,
            imageUrl = it.image
        )
    }.toTypedArray()
}