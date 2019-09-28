package com.atar.galeria.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atar.galeria.utils.Constants

@Database(entities = [Photo::class], version = Constants.CACHE_DB_VERSION)
@TypeConverters(Converters::class)
abstract class CacheDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: CacheDatabase? = null

        fun getDatabase(context: Context): CacheDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CacheDatabase::class.java,
                    Constants.CACHE_DB_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}