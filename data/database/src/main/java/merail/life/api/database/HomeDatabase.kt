package merail.life.api.database

import androidx.room.Database
import androidx.room.RoomDatabase
import merail.life.api.database.model.HomeElementEntity

const val HOME_DATABASE_NAME = "home_database"

@Database(entities = [HomeElementEntity::class], version = 1, exportSchema = false)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun homeElementDao(): HomeElementDao
}