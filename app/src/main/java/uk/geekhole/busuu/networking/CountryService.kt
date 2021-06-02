package uk.geekhole.busuu.networking

import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import uk.geekhole.busuu.models.api.ApiCountry

interface CountryService {

    @GET("all")
    fun getAllCountries(): Single<Response<List<ApiCountry>>>
}