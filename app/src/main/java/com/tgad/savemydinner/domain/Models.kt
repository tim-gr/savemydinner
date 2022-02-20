package com.tgad.savemydinner.domain

data class Recipe(
    val recipeId: Long,
    val title: String,
    val imageUrl: String
)

data class Ingredient(
    val name: String
)