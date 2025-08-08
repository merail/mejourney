package merail.life.data.impl.database

import androidx.room.RoomDatabase
import merail.life.data.impl.database.dto.HomeElementEntity

internal const val HOME_DATABASE_NAME = "home_database"

@androidx.room.Database(entities = [HomeElementEntity::class], version = 1, exportSchema = false)
internal abstract class HomeDatabase : RoomDatabase() {
    abstract fun homeElementDao(): HomeElementDao
}