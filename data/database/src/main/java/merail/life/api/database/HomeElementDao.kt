package merail.life.api.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import merail.life.api.database.model.HomeElementEntity

@Dao
interface HomeElementDao {
    @Query("SELECT * FROM homeElementEntity")
    suspend fun getAll(): List<HomeElementEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(homeElements: List<HomeElementEntity>)
}