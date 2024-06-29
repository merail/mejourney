package merail.life.firebase.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import merail.life.firebase.data.model.HomeElementModel

@Dao
interface HomeElementDao {
    @Query("SELECT * FROM homeElementModel")
    suspend fun getAll(): List<HomeElementModel>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(homeElements: List<HomeElementModel>)
}