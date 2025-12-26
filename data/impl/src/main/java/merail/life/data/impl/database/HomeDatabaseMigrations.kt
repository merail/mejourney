package merail.life.data.impl.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS HomeElementEntity")

        db.execSQL("""
            CREATE TABLE HomeElementEntity (
                id TEXT NOT NULL PRIMARY KEY,
                year INTEGER NOT NULL,
                country TEXT NOT NULL,
                place TEXT NOT NULL,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                image_url TEXT NOT NULL
            )
        """)
    }
}