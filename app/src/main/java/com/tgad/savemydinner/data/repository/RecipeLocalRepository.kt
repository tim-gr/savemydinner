package com.tgad.savemydinner.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tgad.savemydinner.data.api.Network
import com.tgad.savemydinner.data.database.RecipeDatabase
import com.tgad.savemydinner.data.api.NetworkLocal
import com.tgad.savemydinner.data.api.dtos.asDatabaseModel
import com.tgad.savemydinner.data.database.entities.asDomainModel
import com.tgad.savemydinner.domain.entities.Recipe
import com.tgad.savemydinner.domain.repositories.RecipeAbstractRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeLocalRepository(private val database: RecipeDatabase) : RecipeAbstractRepository {

    val recipes: LiveData<List<Recipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asDomainModel()
    }

    override suspend fun refreshRecipes(ingredients: List<String>): Result<Unit?> =
        withContext(Dispatchers.IO) {
            try {
                val newRecipes = NetworkLocal.recipeApi.getRecipesLocalNetworkAsync().await()

                database.recipeDao.clear()
                database.recipeDao.insertAll(*newRecipes.asDatabaseModel())

                return@withContext Result.success(null)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

    override suspend fun findIngredientsWithAutocomplete(search: String): Result<List<String>> {
        val simulatedData = listOf(
            "Apple", "Banana", "Chicken", "Chicory", "Chili", "Flour", "Kiwi", "Mango", "Salad"
        )
        return Result.success(simulatedData.filter { it.startsWith(search) })
    }

    override suspend fun saveRecipe(recipe: Recipe): Result<Unit?> {
        TODO("Not yet implemented")
    }

}