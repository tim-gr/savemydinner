package com.tgad.savemydinner.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tgad.savemydinner.data.database.entities.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1) //, autoMigrations = [AutoMigration(from = 1, to = 2)])
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