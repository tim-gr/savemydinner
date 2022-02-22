package com.tgad.savemydinner.presentation.recipes

import android.app.Application
import androidx.lifecycle.*
import com.tgad.savemydinner.data.database.getDatabase
import com.tgad.savemydinner.data.repository.RecipeRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val recipeRepository = RecipeRepository(database) // RecipeLocalRepository(database)

    val recipes = recipeRepository.recipes

    private var _recipeRequestStatus = MutableLiveData<RecipeRequestStatus>()
    val recipeRequestStatus: LiveData<RecipeRequestStatus>
        get() = _recipeRequestStatus

    private var _autocompleteData = MutableLiveData<List<String>>()
    val autocompleteData: LiveData<List<String>>
        get() = _autocompleteData

    private var _includedIngredients = MutableLiveData<List<String>>()
    val includedIngredients: LiveData<List<String>>
        get() = _includedIngredients

    init {
        _includedIngredients.value = emptyList<String>()
    }

    fun addIncludedIngredient(ingredient: String) {
        val list = _includedIngredients.value?.toMutableList()
        if (list != null && !list.contains(ingredient)) {
            list.add(ingredient)
        }
        _includedIngredients.value = list!!
    }

    fun removeIncludedIngredient(ingredient: String) {
        val list = _includedIngredients.value?.toMutableList()
        list?.remove(ingredient)
        _includedIngredients.value = list!!
    }

    fun searchForRecipes() {
        viewModelScope.launch {
            _recipeRequestStatus.value = RecipeRequestStatus.LOADING
            try {
                recipeRepository.refreshRecipes(_includedIngredients.value!!)
                _recipeRequestStatus.value = RecipeRequestStatus.DONE
            } catch (e: Exception) {
                _recipeRequestStatus.value = RecipeRequestStatus.ERROR
                Timber.e(e)
            }
        }
    }

    fun refreshAutocomplete(search: String) {
        viewModelScope.launch {
            val result = recipeRepository.findIngredientsWithAutocomplete(search)
            _autocompleteData.value = result
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecipesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecipesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct view model")
        }
    }

}

enum class RecipeRequestStatus { LOADING, ERROR, DONE }