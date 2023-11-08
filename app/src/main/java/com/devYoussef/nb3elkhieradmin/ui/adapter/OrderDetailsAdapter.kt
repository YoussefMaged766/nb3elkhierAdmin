package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.databinding.OrderDetailsItemBinding
import com.devYoussef.nb3elkhieradmin.model.OrderDetailsResponse


class OrderDetailsAdapter() :
    ListAdapter<OrderDetailsResponse.UserOrder.Product, OrderDetailsAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<OrderDetailsResponse.UserOrder.Product>() {
        override fun areItemsTheSame(
            oldItem: OrderDetailsResponse.UserOrder.Product,
            newItem: OrderDetailsResponse.UserOrder.Product
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: OrderDetailsResponse.UserOrder.Product,
            newItem: OrderDetailsResponse.UserOrder.Product
        ): Boolean {
            return oldItem._id == newItem._id && oldItem.totalProductPrice == newItem.totalProductPrice && oldItem.quantity == newItem.quantity && oldItem.productId?.name == newItem.productId?.name
        }

    }


    inner class viewholder(var binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderDetailsResponse.UserOrder.Product) {
            binding.txtOrderName.text = data.productId?.name
            binding.txtTotalPrice.text = "${data.totalProductPrice} ${data.productId?.priceCurrency}"
            binding.txtQuantity.text = data.quantity.toString()

        }




    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))
    }
}