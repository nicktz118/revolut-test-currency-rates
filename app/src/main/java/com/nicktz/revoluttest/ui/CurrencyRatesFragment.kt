package com.nicktz.revoluttest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.nicktz.revoluttest.R
import com.nicktz.revoluttest.utils.KeyboardUtil
import kotlinx.android.synthetic.main.fragment_rates.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyRatesFragment : Fragment() {

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
            itemAnimator = null
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == SCROLL_STATE_IDLE) {
                        KeyboardUtil.hideKeyboard(recyclerView)
                    }
                }
            })
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
        viewModel.scrollToBaseCurrency.observe(viewLifecycleOwner, Observer {
            currencyRatesRecyclerView.postDelayed({
                currencyRatesRecyclerView.smoothScrollToPosition(0)
                }, 300
            )
        })
    }

    private fun onItemClicked(currencyRateDisplayItem: CurrencyRateDisplayItem) {
        viewModel.onCurrencyClicked(currencyRateDisplayItem)
    }

    private fun onAmountChanged(amount: Double) {
        viewModel.onAmountChanged(amount)
    }
}