package uk.geekhole.busuu.ui.countries

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import uk.geekhole.busuu.R
import uk.geekhole.busuu.global.RxThrowable
import uk.geekhole.busuu.models.database.Country
import uk.geekhole.busuu.repository.CountryRepository
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(private val countryRepository: CountryRepository) : ViewModel() {

    private val disposables = CompositeDisposable()
    val countries: BehaviorSubject<List<Country>> = BehaviorSubject.create()

    init {
        disposables.add(
            countryRepository
                .getAllCountries()
                .subscribe({
                    countries.onNext(it)
                }, { throw RxThrowable(R.string.internet_error) })
        )
    }

    fun refresh() {
        countryRepository.refreshCountriesFromApi()
    }

    fun performSearch(searchString: String) {
        disposables.add(
            countryRepository
                .getCountriesForSearchParam(searchString)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe({
                    countries.onNext(it)
                }, {
                    throw(it)
                })
        )
    }

    override fun onCleared() {
        super.onCleared()

        disposables.dispose()
        disposables.clear()
    }
}