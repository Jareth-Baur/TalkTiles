package com.jareth.talktiles.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [CategoryTile::class, WordTile::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryTileDao(): CategoryTileDao
    abstract fun wordTileDao(): WordTileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "talk_tiles_db"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                INSTANCE?.let { database ->
                                    // Load and insert categories
                                    val categories = loadCategoryTilesFromJson(context)
                                    database.categoryTileDao().insertAll(categories)

                                    // Load and insert word tiles
                                    val tiles = loadWordTilesFromJson(context)
                                    database.wordTileDao().insertAll(tiles)

                                    Log.d("AppDatabase", "Loaded ${categories.size} categories from JSON")
                                    Log.d("AppDatabase", "Loaded ${tiles.size} word tiles from JSON")

                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun loadWordTilesFromJson(context: Context): List<WordTile> {
            return try {
                val inputStream = context.assets.open("final_aac_word_tiles.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<WordTile>>() {}.type
                Gson().fromJson(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

        private fun loadCategoryTilesFromJson(context: Context): List<CategoryTile> {
            return try {
                val inputStream = context.assets.open("updated_aac_categories.json")
                val json = inputStream.bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<CategoryTile>>() {}.type
                Gson().fromJson(json, type)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }
}
