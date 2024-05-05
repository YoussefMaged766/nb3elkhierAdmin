package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.databinding.OrderDetailsItemBinding
import com.devYoussef.nb3elkhieradmin.model.OrderDetailsResponse


class OrderDetailsAdapter(val listner: OnItemClickListener, val type: String) :
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

    interface OnItemClickListener {
        fun onItemClick(position: Int, item: OrderDetailsResponse.UserOrder.Product)
    }


    inner class viewholder(var binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(data: OrderDetailsResponse.UserOrder.Product) {
            binding.txtOrderName.text = data.productId?.name
            binding.txtTotalPrice.text =
                "${data.totalProductPrice} ${data.productId?.priceCurrency}"
            binding.txtQuantity.text = data.quantity.toString()
            if (type == "current") {
                binding.btnDelete.visibility = ViewGroup.VISIBLE
            } else {
                binding.btnDelete.visibility = ViewGroup.GONE
            }


            if (data.isHasOffer()) {
                with(binding) {
                    offerLayout.visibility = ViewGroup.VISIBLE
                    txtOfferSign.visibility = ViewGroup.VISIBLE
                    if (data.isQuantityLowerThanOffer()){
                        txtOfferSign.visibility = ViewGroup.GONE
                        txtPriceCalculation.visibility = ViewGroup.GONE
//                        txtOfferCalculation.text = "${data.quantity} * ${data.offer?.offerPrice} = ${data.totalProductPrice}"
                        txtOfferCalculation.text = "${data.quantity} * ${data.offer?.offerPrice}"

                    } else{
                        txtOfferSign.visibility = ViewGroup.VISIBLE
                        txtPriceCalculation.visibility = ViewGroup.VISIBLE
//                        txtOfferCalculation.text = "${data.offer?.offerItems} * ${data.offer?.offerPrice} = ${data.calculateOfferPrice()}"
//                        txtPriceCalculation.text = "${data.getQuantityAfterOffer()} * ${data.productPrice} = ${data.calculatePrice()}"
                        txtOfferCalculation.text = "${data.offer?.offerItems} * ${data.offer?.offerPrice}"
                        txtPriceCalculation.text = "${data.getQuantityAfterOffer()} * ${data.productPrice}"
                    }
                }
            }  else{
                binding.offerLayout.visibility = ViewGroup.GONE
                binding.txtOfferSign.visibility = ViewGroup.GONE
//                binding.txtPriceCalculation.text = "${data.quantity} * ${data.productPrice} = ${data.totalProductPrice}"
                binding.txtPriceCalculation.text = "${data.quantity} * ${data.productPrice}"
            }

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.btnDelete.setOnClickListener {
            listner.onItemClick(position, getItem(position))
        }
    }
}