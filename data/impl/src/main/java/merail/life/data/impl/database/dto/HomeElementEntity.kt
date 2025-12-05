package merail.life.data.impl.database.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import merail.life.core.constants.HomeElementsFields
import merail.life.data.api.model.HomeElementModel

@Entity
internal data class HomeElementEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = HomeElementsFields.YEAR) val year: Long,
    @ColumnInfo(name = HomeElementsFields.COUNTRY) val country: String,
    @ColumnInfo(name = HomeElementsFields.PLACE) val place: String,
    @ColumnInfo(name = HomeElementsFields.TITLE) val title: String,
    @ColumnInfo(name = HomeElementsFields.DESCRIPTION) val description: String,
    @ColumnInfo(name = HomeElementsFields.URL) val url: String,
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