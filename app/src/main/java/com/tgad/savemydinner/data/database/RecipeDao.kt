package com.tgad.savemydinner.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tgad.savemydinner.data.database.entities.RecipeEntity

@Dao
interface RecipeDao {
    @Query("select * from recipeentity")
    fun getRecipes(): LiveData<List<RecipeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: RecipeEntity)

    @Query("delete from recipeentity")
    fun clear()
}