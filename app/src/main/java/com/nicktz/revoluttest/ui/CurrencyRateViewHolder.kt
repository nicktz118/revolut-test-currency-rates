package com.nicktz.revoluttest.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.nicktz.revoluttest.R
import kotlinx.android.synthetic.main.item_currency_rate.view.*

data class CurrencyRateDisplayItem(
    val id: String,
    @DrawableRes val flagResId: Int?,
    val title: String,
    val subtitle: String,
    val amount: Double?,
    val editable: Boolean
)

class CurrencyRateViewHolder(
    view: View,
    private val onClickCallBack: (CurrencyRateDisplayItem) -> Unit,
    private val onEditCallBack: (CurrencyRateDisplayItem) -> Unit
) : RecyclerView.ViewHolder(view) {

    private var currencyRate : CurrencyRateDisplayItem? = null

    init {
        view.setOnClickListener {
            currencyRate?.let { item ->
               onClickCallBack(item)
            }
        }
    }

    fun bind(currencyRate: CurrencyRateDisplayItem) = with(itemView) {
        titleTextView.text = currencyRate.title
        subtitleTextView.text = currencyRate.subtitle
        amountEditText.isEnabled = currencyRate.editable
        Glide.with(flagImageView.context)
            .load(currencyRate.flagResId)
            .transform(CircleCrop())
            .into(flagImageView)
    }

    fun updateAmount(currencyRate: CurrencyRateDisplayItem) {
        this.currencyRate = currencyRate
        itemView.amountEditText.setText(currencyRate.amount.toString())
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onClickCallBack: (CurrencyRateDisplayItem) -> Unit,
            onEditCallBack: (CurrencyRateDisplayItem) -> Unit): CurrencyRateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_currency_rate, parent, false)
            return CurrencyRateViewHolder(view, onClickCallBack, onEditCallBack)
        }
    }
}