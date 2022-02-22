package com.tgad.savemydinner.data.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.tgad.savemydinner.data.api.dtos.RecipeDto
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

// Start local Json-Server: json-server --watch resultRecipes1.json --port 8000

interface LocalApi {
    @GET("recipes")
    fun getRecipesLocalNetworkAsync(): Deferred<List<RecipeDto>>
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object NetworkLocal {
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:8000")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val recipeApi: LocalApi = retrofit.create(LocalApi::class.java)
}