package com.tgad.savemydinner.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

// Since we only have one service, this can all go in one file.
// If you add more services, split this to multiple files and make sure to share the retrofit
// object between services.

// Json-Server: json-server --watch resultRecipes1.json --port 8000
// Call 1: https://api.spoonacular.com/recipes/findByIngredients?ingredients=apples,+flour,+sugar&number=5&limitLicense=true&ranking=1&ignorePantry=false&apiKey=7e6c3f535db14f41ae18917aedd0e80c
// Call 2: https://api.spoonacular.com/recipes/findByIngredients?ingredients=noodles,+tomatoes&number=5&limitLicense=true&ranking=1&ignorePantry=false&apiKey=7e6c3f535db14f41ae18917aedd0e80c

interface RecipeService {
    @GET("recipes")
    fun getRecipesLocalNetworkAsync(): Deferred<List<NetworkRecipe>>

    @GET("recipes/findByIngredients?number=6&limitLicense=true&ranking=1&ignorePantry=false&apiKey=7e6c3f535db14f41ae18917aedd0e80c")
    fun getRecipesAsync(@Query("ingredients") ingredients: String): Deferred<List<NetworkRecipe>>

    @GET("food/ingredients/autocomplete?number=3&apiKey=7e6c3f535db14f41ae18917aedd0e80c")
    fun findIngredientsForAutocompleteAsync(@Query("query") query: String): Deferred<List<NetworkIngredient>>
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

/**
 * Main entry point for network access.
 */
object Network {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val recipeApi: RecipeService = retrofit.create(RecipeService::class.java)
}

/**
 * Main entry point for network access when only calling localhost (for development purposes)
 */
object NetworkLocal {
    // Configure retrofit to parse JSON and use coroutines
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val recipeApi: RecipeService = retrofit.create(RecipeService::class.java)
}