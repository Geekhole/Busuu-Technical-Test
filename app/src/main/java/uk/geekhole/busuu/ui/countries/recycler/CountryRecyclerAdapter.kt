package uk.geekhole.busuu.ui.countries.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import uk.geekhole.busuu.databinding.RowCountryBinding
import uk.geekhole.busuu.models.database.Country


class CountryRecyclerAdapter : RecyclerView.Adapter<CountryViewHolder>() {

    private val differ = AsyncListDiffer<Country>(this, DIFF_CALLBACK)

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Country?>() {
            override fun areItemsTheSame(oldItem: Country, newItem: Country) = oldItem == newItem

            override fun areContentsTheSame(oldItem: Country, newItem: Country) = oldItem.areContentsTheSame(newItem)
        }
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(RowCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount() = differ.currentList.count()

    fun updateData(updated: List<Country>) {
        differ.submitList(updated)
    }

    fun getItem(position: Int) = differ.currentList[position]

    override fun getItemId(position: Int) = differ.currentList[position].id
}