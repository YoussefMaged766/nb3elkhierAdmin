package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.databinding.OrderItemBinding
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class OrderAdapter(private val listner : OnItemClickListener) :
    ListAdapter<OrderResponse.AllOrder, OrderAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<OrderResponse.AllOrder>() {
        override fun areItemsTheSame(
            oldItem: OrderResponse.AllOrder,
            newItem: OrderResponse.AllOrder
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: OrderResponse.AllOrder,
            newItem: OrderResponse.AllOrder
        ): Boolean {
            return oldItem._id == newItem._id && oldItem.totalPrice == newItem.totalPrice && oldItem.createdAt == newItem.createdAt && oldItem.orderNum == newItem.orderNum
        }

    }

interface OnItemClickListener {
    fun onItemClick(item: OrderResponse.AllOrder)
}





    inner class viewholder(var binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OrderResponse.AllOrder) {
            binding.txtOrderNumber.text = data.orderNum
            binding.txtOrderDate.text = convertDateToCustomFormat(data.createdAt!!)
            binding.txtOrderTotalPrice.text = data.totalPrice.toString()
            binding.txtShopAddress.text = data.userId?.shopAddress

        }

        private fun convertDateToCustomFormat(inputDate: String): String {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure UTC time zone

            val outputFormat = SimpleDateFormat("hh:mm a|dd-MM-yyyy", Locale.ENGLISH)
            outputFormat.timeZone = TimeZone.getDefault() // Use the default time zone

            val parsedDate: Date? = try {
                inputFormat.parse(inputDate)
            } catch (e: Exception) {
                null
            }

            return if (parsedDate != null) {
                outputFormat.format(parsedDate)
            } else {
                ""
            }
        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.imgArrowDetails.setOnClickListener {
            listner.onItemClick(getItem(position))
        }


    }
}