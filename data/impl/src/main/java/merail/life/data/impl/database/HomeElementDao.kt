package merail.life.data.impl.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import merail.life.data.impl.database.dto.HomeElementEntity

@Dao
internal interface HomeElementDao {
    @Query("SELECT * FROM homeElementEntity")
    fun getAll(): Flow<List<HomeElementEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(homeElements: List<HomeElementEntity>)

    @Query("DELETE FROM homeElementEntity WHERE id NOT IN (:remainingIds)")
    suspend fun deleteMissing(remainingIds: List<String>)

    @Transaction
    suspend fun syncData(entities: List<HomeElementEntity>) {
        val serverIds = entities.map { it.id }

        deleteMissing(serverIds)

        insertAll(entities)
    }
}