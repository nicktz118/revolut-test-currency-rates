package com.nicktz.revoluttest.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nicktz.revoluttest.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CurrencyRatesFragment : Fragment() {

    companion object {
        fun newInstance() = CurrencyRatesFragment()
    }

    private val viewModel: CurrencyRatesViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rates, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}