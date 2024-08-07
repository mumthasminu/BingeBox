package com.example.bingebox.source.local

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.bingebox.model.TVShow
import com.example.bingebox.model.User


@Database(entities = [TVShow::class, User::class], version = 2)
abstract class AppDataBase : RoomDatabase() {
    abstract fun tvShowDao(): TVShowDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBase::class.java,
                    "app_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("AppDataBase", "Database created with version: ${db.version}")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Log.d("AppDataBase", "Database opened with version: ${db.version}")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
