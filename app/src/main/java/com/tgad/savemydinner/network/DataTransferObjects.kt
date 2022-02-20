package com.tgad.savemydinner.network

import com.squareup.moshi.JsonClass
import com.tgad.savemydinner.database.DatabaseRecipe
import com.tgad.savemydinner.domain.Ingredient

@JsonClass(generateAdapter = true)
data class NetworkRecipe(
    val id: Long,
    val title: String,
    val image: String
)

@JsonClass(generateAdapter = true)
data class NetworkIngredient(
    val name: String
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

fun List<NetworkIngredient>.asDomainModel(): List<Ingredient> {
    return this.map {
        Ingredient(
            name = it.name
        )
    }
}