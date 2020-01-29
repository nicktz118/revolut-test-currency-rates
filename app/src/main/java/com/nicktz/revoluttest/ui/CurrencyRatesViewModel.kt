package com.nicktz.revoluttest.ui

import androidx.lifecycle.*
import com.nicktz.revoluttest.data.CurrencyRateRepository
import com.nicktz.revoluttest.dto.CurrencyData
import com.nicktz.revoluttest.utils.network.NetworkState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class CurrencyRatesViewModel(private val repository: CurrencyRateRepository) : ViewModel() {

    sealed class UpdateEvent {
        data class UpdateAmount(val amount: Double) : UpdateEvent()
        data class UpdateBaseCurrency(val currency: String) : UpdateEvent()
        data class UpdateLatestRates(val rates: Map<String, Double>) : UpdateEvent()
    }

    private val currentCurrencyData = CurrencyData()
    private val displayItems = mutableListOf<CurrencyRateDisplayItem>()

    private val _result = MutableLiveData<List<CurrencyRateDisplayItem>>()
    private val _networkState = MutableLiveData<NetworkState>()
    private val _scrollToBaseCurrency = MutableLiveData<Unit>()
    val result: LiveData<List<CurrencyRateDisplayItem>> = _result
    val networkState: LiveData<NetworkState> = _networkState
    val scrollToBaseCurrency: LiveData<Unit> = _scrollToBaseCurrency

    private val eventSubject: PublishSubject<UpdateEvent> = PublishSubject.create()
    private val compositeDisposable = CompositeDisposable()

    init {
        _networkState.value = NetworkState.INITIALIZING
        Observable.merge(eventSubject, getLatestRates())
            .flatMap { event ->
                val items = when (event) {
                    is UpdateEvent.UpdateAmount -> {
                        updateAmount(event.amount)
                    }
                    is UpdateEvent.UpdateLatestRates -> {
                        _networkState.postValue(NetworkState.LOADED)
                        updateRates(event.rates)
                    }
                    is UpdateEvent.UpdateBaseCurrency -> {
                        updateCurrency(event.currency)
                    }
                }
                Observable.just(items)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { items ->
                    updateDisplayItems(items)
                },
                onError = {
                    it.printStackTrace()
                    _networkState.value = NetworkState.error(it.message)
                }
            ).addTo(compositeDisposable)
    }

    private fun updateAmount(amount: Double): List<CurrencyRateDisplayItem> {
        currentCurrencyData.amount = amount
        return displayItems.mapIndexed { index, currencyRateDisplayItem ->
            // no need to update base currency's amount
            if (index == 0) {
                return@mapIndexed currencyRateDisplayItem.copy(amount = amount)
            }
            val newRate = currentCurrencyData.rates[currencyRateDisplayItem.title]
            if (newRate != null) {
                currencyRateDisplayItem.copy(amount = currentCurrencyData.amount.times(newRate))
            } else {
                currencyRateDisplayItem.copy(amount = amount)
            }
        }
    }

    private fun updateRates(rates: Map<String, Double>): List<CurrencyRateDisplayItem> {
        currentCurrencyData.rates = rates
        // when initialization displayItems is empty, so it needs to create whole display items
        return if (displayItems.isEmpty()) {
            val base = CurrencyRateDisplayItem.createBaseCurrency(
                currentCurrencyData.amount,
                currentCurrencyData.base
            )
            val items = currentCurrencyData.rates.map { rateMap ->
                CurrencyRateDisplayItem.createByCurrencyRate(
                    currentCurrencyData.amount,
                    rateMap.key,
                    rateMap.value
                )
            }
            listOf(base).plus(items)
        } else {
            displayItems.map {
                val newRate = rates[it.title]
                if (newRate != null) {
                    it.copy(amount = currentCurrencyData.amount.times(newRate))
                } else {
                    it.copy()
                }
            }
        }
    }

    private fun updateCurrency(baseCurrency: String): List<CurrencyRateDisplayItem> {
        val baseCurrencyRateIndex = displayItems.indexOfFirst { it.title == baseCurrency }
        if (baseCurrencyRateIndex == -1) {
            return displayItems
        }
        // remove base from list
        val newBase = displayItems.removeAt(baseCurrencyRateIndex).copy(editable = true)
        currentCurrencyData.base = newBase.title
        currentCurrencyData.amount = newBase.amount

        // apply currencies behind base
        val newCurrencies = displayItems.map { it.copy(editable = false) }
        return listOf(newBase).plus(newCurrencies)
    }

    private fun getLatestRates(): Observable<UpdateEvent> =
        Observable.interval(0, 1000L, TimeUnit.MILLISECONDS)
            .flatMap { repository.getLatestRates(currentCurrencyData.base).toObservable() }
            .map { UpdateEvent.UpdateLatestRates(it.rates) }

    fun onAmountChanged(amount: Double) {
        eventSubject.onNext(UpdateEvent.UpdateAmount(amount))
    }

    fun onCurrencyClicked(displayItem: CurrencyRateDisplayItem) {
        eventSubject.onNext(UpdateEvent.UpdateBaseCurrency(displayItem.title))
        _scrollToBaseCurrency.value = Unit
    }

    private fun updateDisplayItems(items: List<CurrencyRateDisplayItem>) {
        displayItems.clear()
        displayItems.addAll(items)
        _result.value = items
    }

    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    companion object {
        const val DEFAULT_CURRENCY = "EUR"
        const val DEFAULT_AMOUNT = 100.0
    }
}