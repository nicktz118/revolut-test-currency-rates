package com.nicktz.revoluttest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.nicktz.revoluttest.R
import kotlinx.android.synthetic.main.fragment_rates.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.math.BigDecimal

class CurrencyRatesFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyRatesFragment()
    }

    private val viewModel: CurrencyRatesViewModel by viewModel()

    private val currencyAdapter = CurrencyRateAdapter(::onItemClicked, ::onAmountChanged)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_rates, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currencyRatesRecyclerView.apply {
            adapter = currencyAdapter
        }
        registerObservers()
    }

    private fun registerObservers() {
        viewModel.result.observe(viewLifecycleOwner, Observer { result ->
            currencyAdapter.submitList(result)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer { networkState ->
            currencyAdapter.setNetworkState(networkState)
        })
    }

    private fun onItemClicked(currencyRateDisplayItem: CurrencyRateDisplayItem) {
        viewModel.onCurrencyClicked(currencyRateDisplayItem)
    }

    private fun onAmountChanged(amount: Double) {
        viewModel.onAmountChanged(amount)
    }
}