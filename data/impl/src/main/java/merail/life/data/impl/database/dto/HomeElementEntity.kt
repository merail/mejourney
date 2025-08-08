package merail.life.data.impl.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import merail.life.data.api.model.HomeElementModel

@Entity
internal data class HomeElementEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "year") val year: Long,
    @ColumnInfo(name = "country") val country: String,
    @ColumnInfo(name = "place") val place: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "url") val url: String,
)

internal fun HomeElementEntity.toModel() = HomeElementModel(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)

internal fun HomeElementModel.toEntity() = HomeElementEntity(
    id = id,
    year = year,
    country = country,
    place = place,
    title = title,
    description = description,
    url = url,
)