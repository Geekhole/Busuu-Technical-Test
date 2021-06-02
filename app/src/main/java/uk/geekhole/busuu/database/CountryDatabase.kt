package uk.geekhole.busuu.database

import androidx.room.Database
import androidx.room.RoomDatabase
import uk.geekhole.busuu.models.database.Country

@Database(entities = [Country::class], version = 1, exportSchema = true)
abstract class CountryDatabase : RoomDatabase() {

    abstract fun countryDao(): CountryDao
}