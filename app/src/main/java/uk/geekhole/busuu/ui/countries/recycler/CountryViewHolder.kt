package uk.geekhole.busuu.ui.countries.recycler

import androidx.recyclerview.widget.RecyclerView
import uk.geekhole.busuu.databinding.RowCountryBinding
import uk.geekhole.busuu.models.database.Country
import uk.geekhole.busuu.ui.global.ItemClickSupportViewHolder

class CountryViewHolder(private val binding: RowCountryBinding) : RecyclerView.ViewHolder(binding.root), ItemClickSupportViewHolder {

    override val isClickable = true
    override val isLongClickable = false

    fun bind(country: Country) {
        binding.country = country
        binding.executePendingBindings()
    }
}