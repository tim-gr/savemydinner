package com.tgad.savemydinner.data.repository

import com.tgad.savemydinner.data.database.RecipeDatabase
import com.tgad.savemydinner.data.api.NetworkLocal
import com.tgad.savemydinner.data.api.dtos.asDatabaseModel
import com.tgad.savemydinner.domain.entities.Recipe
import com.tgad.savemydinner.domain.repositories.RecipeAbstractRepository

class RecipeLocalRepository(private val database: RecipeDatabase) : RecipeAbstractRepository {

    override suspend fun refreshRecipes(ingredients: List<String>) {
        val newRecipes = NetworkLocal.recipeApi.getRecipesLocalNetworkAsync().await()
        database.recipeDao.clear()
        database.recipeDao.insertAll(*newRecipes.asDatabaseModel())
    }

    override suspend fun findIngredientsWithAutocomplete(search: String): List<String> {
        val simulatedData = listOf(
            "Apple", "Banana", "Chicken", "Chicory", "Chili", "Flour", "Kiwi", "Mango", "Salad"
        )
        return simulatedData.filter { it.startsWith(search) }
    }

    override suspend fun saveRecipeAsFavorite(recipe: Recipe) {
        TODO("Not yet implemented")
    }

}