package com.tgad.savemydinner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tgad.savemydinner.database.RecipeDatabase
import com.tgad.savemydinner.database.asDomainModel
import com.tgad.savemydinner.domain.Ingredient
import com.tgad.savemydinner.domain.Recipe
import com.tgad.savemydinner.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val database: RecipeDatabase) {

    private val useRealApi = true

    val recipes: LiveData<List<Recipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asDomainModel()
    }

    // Use of Coroutine => suspend
    suspend fun refreshRecipes(ingredients: List<String>) {
        withContext(Dispatchers.IO) { // Disk IO would otherwise block the main thread

            val recipes = if (useRealApi) {
                val ingredientsString = ingredients.joinToString(",+")
                Network.recipeApi.getRecipesAsync(ingredientsString).await()
            } else {
                NetworkLocal.recipeApi.getRecipesLocalNetworkAsync().await()
            }

            // All database calls here (in this context)!
            database.recipeDao.clear()
            database.recipeDao.insertAll(*recipes.asDatabaseModel())
        }
    }

    suspend fun findIngredientsWithAutocomplete(search: String): List<String> {
        return if (useRealApi) {
            var result: List<Ingredient>
            withContext(Dispatchers.IO) {
                result = Network.recipeApi.findIngredientsForAutocompleteAsync(search).await()
                    .asDomainModel()
            }
            result.map { it.name }
        } else {
            val simulatedData = listOf(
                "Apple", "Banana", "Chicken", "Chicory", "Chili", "Flour", "Kiwi", "Mango", "Salad"
            )
            return simulatedData.filter { it.startsWith(search) }
        }
    }

}
