package com.tgad.savemydinner.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.tgad.savemydinner.database.RecipeDatabase
import com.tgad.savemydinner.database.asDomainModel
import com.tgad.savemydinner.domain.Recipe
import com.tgad.savemydinner.network.Network
import com.tgad.savemydinner.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipeRepository(private val database: RecipeDatabase) {

    val recipes: LiveData<List<Recipe>> = Transformations.map(database.recipeDao.getRecipes()) {
        it.asDomainModel()
    }

    // Use of Coroutine => suspend
    suspend fun refreshRecipes() {
        withContext(Dispatchers.IO) { // Disk IO would otherwise block the main thread
            // All database calls here!
            val recipes = Network.recipeApi.getRecipesAsync().await()
            database.recipeDao.clear()
            database.recipeDao.insertAll(*recipes.asDatabaseModel())
        }
    }

}
