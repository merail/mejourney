package merail.life.data.impl.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import merail.life.data.impl.database.dto.HomeElementEntity

@Dao
internal interface HomeElementDao {
    @Query("SELECT * FROM homeElementEntity")
    fun getAll(): Flow<List<HomeElementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(homeElements: List<HomeElementEntity>)
}