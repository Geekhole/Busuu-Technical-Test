package uk.geekhole.busuu.ui.countrydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import uk.geekhole.busuu.R
import uk.geekhole.busuu.databinding.FragmentCountryDetailsBinding
import uk.geekhole.busuu.global.FragmentArgumentDelegate
import uk.geekhole.busuu.ui.global.BaseFragment
import javax.inject.Inject

@AndroidEntryPoint
class CountryDetailsFragment : BaseFragment(R.layout.fragment_country_details) {

    private lateinit var binding: FragmentCountryDetailsBinding
    private var countryId: String by FragmentArgumentDelegate

    @Inject
    lateinit var countryViewModelAssistedFactory: CountryDetailsViewModel.AssistedFactory
    private val viewModel: CountryDetailsViewModel by viewModels {
        CountryDetailsViewModel.provideFactory(countryViewModelAssistedFactory, countryId)
    }

    companion object {
        fun newInstance(countryId: String) = CountryDetailsFragment().apply {
            this.countryId = countryId
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCountryDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(viewModel.country.subscribe {
            binding.country = it
            binding.executePendingBindings()
        })
    }
}