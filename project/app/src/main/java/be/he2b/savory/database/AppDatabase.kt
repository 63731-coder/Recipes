package be.he2b.savory.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import be.he2b.savory.R
import be.he2b.savory.model.Recipe

/*
 * The main database class for the application.
 * It serves as the single point of access to the persisted data and manages the database instance.
 */
@Database(entities = [Recipe::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao // used for the SQL queries (insert, delete...)

    companion object {
        @Volatile // make sure that all threads use the same db instance
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                // create db if it doesn't exist
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    context.getString(R.string.savory_database)
                ).fallbackToDestructiveMigration(false).build()

                INSTANCE = instance
            }
            return INSTANCE ?: throw IllegalStateException("Database not initialized")
        }
    }
}