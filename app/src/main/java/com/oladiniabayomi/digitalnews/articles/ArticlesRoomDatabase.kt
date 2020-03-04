package com.oladiniabayomi.digitalnews.articles

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.oladiniabayomi.digitalarticles.articles.Articles
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Articles::class], version = 1, exportSchema = false)
abstract class ArticlesRoomDatabase : RoomDatabase() {

    abstract fun articlesDao(): ArticlesDao

    companion object {
        @Volatile
        private var INSTANCE: ArticlesRoomDatabase? = null

        fun getDatabase(context: Context): ArticlesRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArticlesRoomDatabase::class.java,
                    "articles_database"
                ).build()
                INSTANCE = instance
                return instance
            }


        }

    }

}