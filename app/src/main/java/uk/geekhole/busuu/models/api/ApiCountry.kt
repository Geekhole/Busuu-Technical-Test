package uk.geekhole.busuu.models.api

data class ApiCountry(val name: String, val capital: String, val flag: String, var currencies: List<ApiCurrency?>)