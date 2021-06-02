package uk.geekhole.busuu.ui.global

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import uk.geekhole.busuu.R
import uk.geekhole.busuu.models.database.Country

@BindingAdapter("android:src")
fun setImageFromUrl(view: ImageView, country: Country) {
    ImageHelper.loadImage(country, view)
}

@BindingAdapter("countryCapital")
fun setCountryCapital(view: TextView, capital: String?) {
    view.text = view.context.getString(R.string.country_capital, capital)
}

@BindingAdapter("countryCurrencies")
fun setCountryCurrencies(view: TextView, currencies: String?) {
    view.text = view.context.getString(R.string.country_currencies, currencies)
}