package com.tgad.savemydinner.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tgad.savemydinner.data.database.RecipeDatabase
import com.tgad.savemydinner.data.database.entities.asDomainModel
import com.tgad.savemydinner.data.api.*
import com.tgad.savemydinner.data.api.dtos.asDatabaseModel
import com.tgad.savemydinner.data.api.dtos.asDomainModel
import com.tgad.savemydinner.domain.entities.Recipe
import com.tgad.savemydinner.domain.repositories.RecipeAbstractRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val database: RecipeDatabase) : RecipeAbstractRepository {

    val recipes: LiveData<List<Recipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asDomainModel()
    }

    // Use of Coroutine => suspend
    // Disk IO would otherwise block the main thread
    override suspend fun refreshRecipes(ingredients: List<String>): Result<Unit?> =
        withContext(Dispatchers.IO) {
            try {
                val ingredientsString = ingredients.joinToString(",+")
                val newRecipes = Network.recipeApi.getRecipesAsync(ingredientsString).await()

                // All database calls here (in this context)
                database.recipeDao.clear()
                database.recipeDao.insertAll(*newRecipes.asDatabaseModel())

                return@withContext Result.success(null)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

    override suspend fun findIngredientsWithAutocomplete(search: String): Result<List<String>> =
        withContext(Dispatchers.IO) {
            try {
                val ingredients = Network.recipeApi.findIngredientsForAutocompleteAsync(search).await()
                    .asDomainModel().map { it.name }
                return@withContext Result.success(ingredients)
            } catch (e: Exception) {
                return@withContext Result.failure(e)
            }
        }

    override suspend fun saveRecipe(recipe: Recipe): Result<Unit?> {
        TODO("Not yet implemented")
    }

}
