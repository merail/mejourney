package merail.life.firebase.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import merail.life.firebase.data.model.HomeElementModel

const val HOME_DATABASE_NAME = "home_database"

@Database(entities = [HomeElementModel::class], version = 1)
abstract class HomeDatabase : RoomDatabase() {
    abstract fun homeElementDao(): HomeElementDao
}