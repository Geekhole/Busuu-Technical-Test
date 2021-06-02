package uk.geekhole.busuu.ui.countrydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import uk.geekhole.busuu.repository.CountryRepository

class CountryDetailsViewModel @AssistedInject constructor(countryRepository: CountryRepository, @Assisted private val countryId: String) : ViewModel() {

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(countryId: String): CountryDetailsViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: AssistedFactory, countryId: String): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(countryId) as T
            }
        }
    }

    var country = countryRepository.getCountryById(countryId)
}