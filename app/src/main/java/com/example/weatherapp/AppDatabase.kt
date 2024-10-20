package com.example.weatherapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase




//@Database(entities = [User::class], version = 3)
//abstract class AppDatabase : RoomDatabase() {
//
//    abstract fun userDao(): UserDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        private val MIGRATION_2_3 = object :
//            Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE User ADD COLUMN new_column_name TEXT")
//            }
//        }
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "weatherapp_database"
//                )
//                    .addMigrations(MIGRATION_2_3)
//                    .fallbackToDestructiveMigration()  // This line allows destructive migrations
//                    .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

@Database(entities = [User::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "weatherapp_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


