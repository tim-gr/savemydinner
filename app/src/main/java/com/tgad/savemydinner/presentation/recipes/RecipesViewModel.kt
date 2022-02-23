package com.tgad.savemydinner.presentation.recipes

import android.app.Application
import androidx.lifecycle.*
import com.tgad.savemydinner.data.database.getDatabase
import com.tgad.savemydinner.data.repository.RecipeLocalRepository
import com.tgad.savemydinner.data.repository.RecipeRepository
import com.tgad.savemydinner.presentation.common.LoadingStatus
import kotlinx.coroutines.launch

class RecipesViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val recipeRepository = RecipeRepository(database) // RecipeLocalRepository(database)

    val recipes = recipeRepository.recipes

    private var _loadingStatus = MutableLiveData<LoadingStatus>()
    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus

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
        list?.add(ingredient)
        if (list != null) {
            _includedIngredients.value = list!!
        }
    }

    fun removeIncludedIngredient(ingredient: String) {
        val list = _includedIngredients.value?.toMutableList()
        list?.remove(ingredient)
        if (list != null) {
            _includedIngredients.value = list!!
        }
    }

    fun searchForRecipes() {
        viewModelScope.launch {
            _loadingStatus.value = LoadingStatus.LOADING
            val result = recipeRepository.refreshRecipes(_includedIngredients.value!!)
            if (result.isSuccess) {
                _loadingStatus.value = LoadingStatus.DONE
            } else {
                _loadingStatus.value = LoadingStatus.ERROR
            }
        }
    }

    fun refreshAutocomplete(search: String) {
        viewModelScope.launch {
            val result = recipeRepository.findIngredientsWithAutocomplete(search)
            if (result.isSuccess) {
                _autocompleteData.value = result.getOrNull()
            }
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