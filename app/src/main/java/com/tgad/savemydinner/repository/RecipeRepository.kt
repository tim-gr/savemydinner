package com.tgad.savemydinner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tgad.savemydinner.database.RecipeDatabase
import com.tgad.savemydinner.database.asDomainModel
import com.tgad.savemydinner.domain.Ingredient
import com.tgad.savemydinner.domain.Recipe
import com.tgad.savemydinner.network.Network
import com.tgad.savemydinner.network.NetworkLocal
import com.tgad.savemydinner.network.asDatabaseModel
import com.tgad.savemydinner.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val database: RecipeDatabase) {

    val recipes: LiveData<List<Recipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asDomainModel()
    }

    // Use of Coroutine => suspend
    suspend fun refreshRecipes(ingredients: List<String>) {
        withContext(Dispatchers.IO) { // Disk IO would otherwise block the main thread

            val ingredientsString = ingredients.joinToString(",+")
            val recipes = Network.recipeApi.getRecipesAsync(ingredientsString).await()

            // Uncomment when local data is supposed to be used instead of the real API.
            //val recipes = NetworkLocal.recipeApi.getRecipesLocalNetworkAsync().await()

            // All database calls here (in this context)!
            database.recipeDao.clear()
            database.recipeDao.insertAll(*recipes.asDatabaseModel())
        }
    }

    suspend fun findIngredientsWithAutocomplete(search: String): List<Ingredient> {
        var result: List<Ingredient>
        withContext(Dispatchers.IO) {
            result = Network.recipeApi.findIngredientsForAutocompleteAsync(search).await().asDomainModel()
        }
        return result
    }

}
