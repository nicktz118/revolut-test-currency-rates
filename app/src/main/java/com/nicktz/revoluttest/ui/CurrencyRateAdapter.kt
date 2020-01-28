package com.nicktz.revoluttest.ui

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nicktz.revoluttest.utils.network.NetworkState
import com.nicktz.revoluttest.utils.network.NetworkStateItemViewHolder
import com.nicktz.revoluttest.utils.toBigDecimalWith2digits

class CurrencyRateAdapter(private val onClickCallBack: (CurrencyRateDisplayItem) -> Unit,
                          private val onAmountChanged: (Double) -> Unit)
    : ListAdapter<CurrencyRateDisplayItem, RecyclerView.ViewHolder>(CURRENCY_RATE_COMPARATOR) {

    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            VIEW_TYPE_CURRENCY_RATE -> (holder as CurrencyRateViewHolder).bind(getItem(position))
            VIEW_TYPE_NETWORK -> (holder as NetworkStateItemViewHolder).bindTo(
                networkState
            )
        }
    }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            val item = getItem(position)
            payloads.forEach { payload ->
                when(payload) {
                    CurrencyRatePayload.AMOUNT -> {
                        (holder as CurrencyRateViewHolder).updateAmount(item)
                    }
                    CurrencyRatePayload.EDITABLE -> {
                        (holder as CurrencyRateViewHolder).updateAmountEditable(item)
                    }
                }
            }
        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CURRENCY_RATE -> CurrencyRateViewHolder.create(parent, onClickCallBack, onAmountChanged)
            VIEW_TYPE_NETWORK -> NetworkStateItemViewHolder.create(parent)
            else -> error("unknown view type")
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            VIEW_TYPE_NETWORK
        } else {
            VIEW_TYPE_CURRENCY_RATE
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    companion object {
        private const val VIEW_TYPE_NETWORK = 0
        private const val VIEW_TYPE_CURRENCY_RATE = 1

        enum class CurrencyRatePayload {
            AMOUNT, EDITABLE
        }

        val CURRENCY_RATE_COMPARATOR = object : DiffUtil.ItemCallback<CurrencyRateDisplayItem>() {
            override fun areContentsTheSame(oldItem: CurrencyRateDisplayItem, newItem: CurrencyRateDisplayItem): Boolean =
                oldItem == newItem

            override fun areItemsTheSame(oldItem: CurrencyRateDisplayItem, newItem: CurrencyRateDisplayItem): Boolean =
                oldItem.title == newItem.title

            override fun getChangePayload(oldItem: CurrencyRateDisplayItem, newItem: CurrencyRateDisplayItem): Any? {
                return when {
                    sameAmount(oldItem, newItem) -> CurrencyRatePayload.AMOUNT
                    sameEditable(oldItem, newItem) -> CurrencyRatePayload.EDITABLE
                    else -> { null }
                }
            }
        }

        private fun sameAmount(oldItem: CurrencyRateDisplayItem, newItem: CurrencyRateDisplayItem): Boolean {
            return oldItem.amount.toBigDecimalWith2digits() != newItem.amount.toBigDecimalWith2digits()
        }

        private fun sameEditable(oldItem: CurrencyRateDisplayItem, newItem: CurrencyRateDisplayItem): Boolean {
            return oldItem.editable != newItem.editable
        }
    }
}