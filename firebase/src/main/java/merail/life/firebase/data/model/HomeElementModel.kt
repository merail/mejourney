package merail.life.firebase.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HomeElementModel(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "year") val year: Long,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "url") val url: String,
)