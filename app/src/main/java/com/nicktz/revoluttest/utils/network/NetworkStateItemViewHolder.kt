package com.nicktz.revoluttest.utils.network

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.nicktz.revoluttest.R
import kotlinx.android.synthetic.main.item_network_state.view.*

class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun bindTo(networkState: NetworkState?) = with(itemView) {
        loading.isVisible = networkState?.status == Status.RUNNING
        msgTextView.isVisible = networkState?.msg != null
    }

    companion object {
        fun create(parent: ViewGroup): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_network_state, parent, false)
            return NetworkStateItemViewHolder(view)
        }
    }
}