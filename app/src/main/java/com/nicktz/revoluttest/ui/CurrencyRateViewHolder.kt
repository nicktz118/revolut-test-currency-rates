package com.nicktz.revoluttest.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.nicktz.revoluttest.R
import com.nicktz.revoluttest.utils.CurrencyFlags
import com.nicktz.revoluttest.utils.toBigDecimalWith2digits
import kotlinx.android.synthetic.main.item_currency_rate.view.*

data class CurrencyRateDisplayItem(
    @DrawableRes val flagResId: Int?,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val editable: Boolean
) {
    companion object {
        fun createByCurrencyRate(
            amount: Double,
            currency: String,
            rate: Double
        ): CurrencyRateDisplayItem = CurrencyRateDisplayItem(
            flagResId = CurrencyFlags.byCurrency[currency],
            title = currency,
            subtitle = java.util.Currency.getInstance(currency).displayName,
            amount = rate.times(amount),
            editable = false
        )

        fun createBaseCurrency(
            amount: Double,
            currency: String
        ): CurrencyRateDisplayItem = CurrencyRateDisplayItem(
            flagResId = CurrencyFlags.byCurrency[currency],
            title = currency,
            subtitle = java.util.Currency.getInstance(currency).displayName,
            amount = amount,
            editable = true
        )
    }
}

class CurrencyRateViewHolder(
    view: View,
    private val onClickCallBack: (CurrencyRateDisplayItem) -> Unit,
    private val onAmountChanged: (Double) -> Unit
) : RecyclerView.ViewHolder(view) {

    private lateinit var currencyRate: CurrencyRateDisplayItem

    private var textWatcher: TextWatcher = object : TextWatcher {

        override fun afterTextChanged(s: Editable?) {
            s?.let {
                if (it.toString().isEmpty().not() || it.toString().isBlank().not()) {
                    onAmountChanged.invoke(it.toString().toDouble())
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    init {
        view.setOnClickListener {
            onClickCallBack(currencyRate)
        }
    }

    fun bind(item: CurrencyRateDisplayItem) = with(itemView) {
        itemView.amountEditText.removeTextChangedListener(textWatcher)
        currencyRate = item
        titleTextView.text = item.title
        subtitleTextView.text = item.subtitle
        Glide.with(flagImageView.context)
            .load(item.flagResId)
            .transform(CircleCrop())
            .into(flagImageView)
        updateAmount(item)
        updateAmountEditable(item)
    }

    fun updateAmount(currencyRate: CurrencyRateDisplayItem) {
        this.currencyRate = currencyRate
        val text = itemView.amountEditText.text
        val editText = itemView.amountEditText
        if (currencyRate.editable
            && text.toString() == currencyRate.amount
                .toBigDecimal().stripTrailingZeros().toPlainString()
        ) {
            return
        } else if ((text.isNullOrEmpty() || text.isNullOrBlank()) && currencyRate.editable) {
            editText.setText(currencyRate.amount
                .toBigDecimal().stripTrailingZeros().toPlainString())
        } else {
            editText.setText(currencyRate.amount
                .toBigDecimalWith2digits().toPlainString())
        }
    }

    fun updateAmountEditable(currencyRate: CurrencyRateDisplayItem) {
        this.currencyRate = currencyRate
        itemView.amountEditText.isEnabled = currencyRate.editable
        if (currencyRate.editable) {
            itemView.amountEditText.addTextChangedListener(textWatcher)
        } else {
            itemView.amountEditText.removeTextChangedListener(textWatcher)
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClickCallBack: (CurrencyRateDisplayItem) -> Unit,
            onAmountChanged: (Double) -> Unit
        ): CurrencyRateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency_rate, parent, false)
            return CurrencyRateViewHolder(view, onClickCallBack, onAmountChanged)
        }
    }
}
