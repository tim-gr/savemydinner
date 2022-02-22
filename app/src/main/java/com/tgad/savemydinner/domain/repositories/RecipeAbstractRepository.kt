package com.tgad.savemydinner.domain.repositories

import com.tgad.savemydinner.domain.entities.Recipe

interface RecipeAbstractRepository {

    suspend fun refreshRecipes(ingredients: List<String>)

    suspend fun findIngredientsWithAutocomplete(search: String): List<String>

    suspend fun saveRecipeAsFavorite(recipe: Recipe)

}