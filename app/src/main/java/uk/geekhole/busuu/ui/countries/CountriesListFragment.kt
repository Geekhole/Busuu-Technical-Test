package uk.geekhole.busuu.ui.countries

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import uk.geekhole.busuu.R
import uk.geekhole.busuu.databinding.FragmentCountriesListBinding
import uk.geekhole.busuu.models.database.Country
import uk.geekhole.busuu.ui.countries.recycler.CountryRecyclerAdapter
import uk.geekhole.busuu.ui.countrydetails.CountryDetailsFragment
import uk.geekhole.busuu.ui.global.BaseFragment
import uk.geekhole.busuu.ui.global.onItemClick

@AndroidEntryPoint
class CountriesListFragment : BaseFragment(R.layout.fragment_countries_list) {

    private lateinit var binding: FragmentCountriesListBinding

    private val viewModel: CountriesViewModel by viewModels()

    private var adapter = CountryRecyclerAdapter()

    companion object {
        fun newInstance() = CountriesListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCountriesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(viewModel.countries
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it.count() == 0) return@subscribe

                binding.emptyStateLayout.visibility = View.GONE
                updateAdapter(it)
            })

        binding.countriesList.onItemClick { _, position, _ ->
            fragmentManager.showFragment(CountryDetailsFragment.newInstance(adapter.getItem(position).name), false)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.performSearch(newText.orEmpty())
                return false
            }
        })

        binding.refresh.setOnClickListener {
            viewModel.refresh()
        }
    }

    private fun updateAdapter(data: List<Country>) {
        adapter.updateData(data)
        if (binding.countriesList.adapter != adapter) binding.countriesList.adapter = adapter
    }
}