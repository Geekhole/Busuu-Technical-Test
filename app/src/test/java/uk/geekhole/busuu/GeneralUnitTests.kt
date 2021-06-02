package uk.geekhole.busuu

import io.reactivex.Flowable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import uk.geekhole.busuu.models.api.ApiCountry
import uk.geekhole.busuu.models.api.ApiCurrency
import uk.geekhole.busuu.repository.CountryRepository
import uk.geekhole.busuu.ui.countries.CountriesViewModel

/**
 * These currently don't work due to an error that I haven't had time to look into properly:
 * "java.lang.IllegalStateException: Could not initialize plugin: interface org.mockito.plugins.MockMaker (alternate: null)"
 * */
class GeneralUnitTests {

    companion object {
        const val COUNTRY_NAME = "Test"
        const val CAPITAL_NAME = "Testing"
        const val FLAG = "Flag"

        val currencies = listOf(ApiCurrency("CODE_1", "NAME_1", "SYMBOL_1"), ApiCurrency("CODE_2", "NAME_2", "SYMBOL_2"))
        val apiCountry = ApiCountry(COUNTRY_NAME, CAPITAL_NAME, FLAG, currencies)
        val apiCountriesList = listOf(apiCountry)
    }

    @Mock
    lateinit var countryRepository: CountryRepository

    @Mock
    lateinit var countriesViewModel: CountriesViewModel

    @Before
    fun init() {
        MockitoAnnotations.openMocks(this)
    }


    @Test
    fun `check api to db model mapping succeeds`() {
        val dbModels = CountryRepository.apiToDbModel(listOf(apiCountry))
        assert(
            dbModels.count() == 1
                    && dbModels[0].name == COUNTRY_NAME
                    && dbModels[0].capital == CAPITAL_NAME
                    && dbModels[0].flag == FLAG
                    && dbModels[0].currencies == currencies.joinToString(",") { it.code }
        )
    }

    @Test
    fun `check initial subscription returns results`() {
        `when`(countryRepository.getAllCountries()).thenReturn(Flowable.just(CountryRepository.apiToDbModel(apiCountriesList)))
        val observer = countriesViewModel.countries.test()

        observer.assertNoTimeout()
        observer.assertSubscribed()
        observer.assertValue {
            it.count() == 1
                    && it[0].name == COUNTRY_NAME
                    && it[0].capital == CAPITAL_NAME
                    && it[0].flag == FLAG
                    && it[0].currencies == currencies.joinToString(",") { s -> s.code }
        }
    }
}