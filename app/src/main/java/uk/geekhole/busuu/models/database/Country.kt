package uk.geekhole.busuu.models.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class Country(@PrimaryKey val name: String, val capital: String, val flag: String, var currencies: String) {
    
    val id: Long
        get() = name.hashCode().toLong()

    fun areContentsTheSame(item: Country): Boolean {
        return item.name == name
                && item.capital == capital
                && item.flag == flag
                && item.currencies == currencies
    }
}