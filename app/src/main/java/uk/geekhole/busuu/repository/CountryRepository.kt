package uk.geekhole.busuu.repository

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uk.geekhole.busuu.R
import uk.geekhole.busuu.database.CountryDao
import uk.geekhole.busuu.global.RxThrowable
import uk.geekhole.busuu.models.api.ApiCountry
import uk.geekhole.busuu.models.database.Country
import uk.geekhole.busuu.networking.CountryService
import javax.inject.Inject


open class CountryRepository @Inject constructor(private val countryService: CountryService, private val countryDao: CountryDao) {

    private val disposables = CompositeDisposable()

    companion object {
        fun apiToDbModel(api: List<ApiCountry>?): List<Country> {
            if (api == null) return listOf()
            val dbModels = mutableListOf<Country>()

            api.forEach {
                dbModels.add(Country(it.name, it.capital, it.flag, it.currencies.mapNotNull { c -> c?.code }.joinToString(",")))
            }

            return dbModels
        }
    }

    fun getAllCountries(): Flowable<List<Country>> {
        return countryDao.getAllCountries()
            .subscribeOn(Schedulers.io())
            .doOnSubscribe {
                refreshCountriesFromApi()
            }
    }

    /** Doesn't refresh the data on subscription as it is so unlikely to change. For data that may change at any point adding a doOnSubscribe to refresh the data for the specific item would be
     * a good idea */
    fun getCountryById(id: String): Flowable<Country> {
        return countryDao.getById(id)
            .subscribeOn(Schedulers.io())
    }

    fun getCountriesForSearchParam(searchString: String): Single<List<Country>> {
        return countryDao.getCountriesForSearchParam("%$searchString%")
    }

    fun refreshCountriesFromApi() {
        val disposable = countryService.getAllCountries()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .toObservable()
            .switchMapCompletable { response ->
                if (!response.isSuccessful || response.code() != 200) {
                    // Throw an error to be caught by the global error handler and display the message
                    throw(RxThrowable(R.string.error_fetching_countries))
                }

                countryDao.insert(apiToDbModel(response.body()))
            }.subscribe({ }, { throw RxThrowable(R.string.internet_error) })

        disposables.add(disposable)
    }

}