package com.tgad.savemydinner.findrecipes

import android.app.Application
import androidx.lifecycle.*
import com.tgad.savemydinner.R
import com.tgad.savemydinner.database.getDatabase
import com.tgad.savemydinner.repository.RecipeRepository
import kotlinx.coroutines.launch

class FindRecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val recipeRepository = RecipeRepository(database)
    val recipes = recipeRepository.recipes

    private var _includedIngredients = MutableLiveData<List<String>>()
    val includedIngredients: LiveData<List<String>>
        get() = _includedIngredients

    private val availableIngredients: Array<out String> =
        application.resources.getStringArray(R.array.ingredients_array)

    init {
        _includedIngredients.value = emptyList<String>()
    }

    fun getAvailableIngredients(): Array<out String> {
        // TODO: Filling database with available ingredients (at the first call?)
        return availableIngredients
    }

    fun addIncludedIngredient(ingredient: String) {
        val list = _includedIngredients.value?.toMutableList()
        if (list != null && !list.contains(ingredient)) {
            list.add(ingredient)
        }
        _includedIngredients.value = list!!
    }

    fun removeIngredient(ingredient: String) {
        val list = _includedIngredients.value?.toMutableList()
        list?.remove(ingredient)
        _includedIngredients.value = list!!
    }

    fun searchForRecipes() {
        viewModelScope.launch {
            recipeRepository.refreshRecipes()
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FindRecipesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FindRecipesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}