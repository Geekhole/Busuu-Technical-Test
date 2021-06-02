package uk.geekhole.busuu.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import uk.geekhole.busuu.models.database.Country

@Dao
interface CountryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: List<Country>): Completable

    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flowable<List<Country>>

    @Query("SELECT * FROM countries WHERE name = :id")
    fun getById(id: String): Flowable<Country>

    @Query("SELECT * FROM countries WHERE name LIKE :searchString")
    fun getCountriesForSearchParam(searchString: String): Single<List<Country>>


}