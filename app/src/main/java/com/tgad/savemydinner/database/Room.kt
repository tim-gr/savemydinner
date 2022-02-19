package com.tgad.savemydinner.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RecipeDao {
    @Query("select * from databaserecipe")
    fun getRecipes(): LiveData<List<DatabaseRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg videos: DatabaseRecipe)

    @Query("delete from databaserecipe")
    fun clear()
}

@Database(entities = [DatabaseRecipe::class], version = 1)//, autoMigrations = [AutoMigration(from = 1, to = 2)])
abstract class RecipeDatabase : RoomDatabase() {
    abstract val recipeDao: RecipeDao
}

private lateinit var INSTANCE: RecipeDatabase

fun getDatabase(context: Context): RecipeDatabase {
    synchronized(RecipeDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java, "recipe"
            ).build()
        }
    }
    return INSTANCE
}