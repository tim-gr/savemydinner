package com.tgad.savemydinner.domain.repositories

import com.tgad.savemydinner.domain.entities.Recipe

interface RecipeAbstractRepository {

    /**
     * Update the recipes in the database with recipes from a call
     * of the Spoonacular API, including the passed ingredients.
     */
    suspend fun refreshRecipes(ingredients: List<String>): Result<Unit?>

    /**
     * Returns ingredients that fit the given search string,
     * calling the Spoonacular API autocomplete endpoint.
     */
    suspend fun findIngredientsWithAutocomplete(search: String): Result<List<String>>

    /**
     * Update the given recipe in the database.
     */
    suspend fun saveRecipe(recipe: Recipe): Result<Unit?>

}