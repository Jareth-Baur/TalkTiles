package com.jareth.talktiles.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WordTile::class], version = 1)
abstract class TalkTilesDatabase : RoomDatabase() {
    abstract fun wordTileDao(): WordTileDao

    companion object {
        @Volatile private var INSTANCE: TalkTilesDatabase? = null

        fun getDatabase(context: Context): TalkTilesDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    TalkTilesDatabase::class.java,
                    "talk_tiles_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
