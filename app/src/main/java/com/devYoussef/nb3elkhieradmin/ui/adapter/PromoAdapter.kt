package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.databinding.PromoCodeItemBinding
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse

class PromoAdapter(private var listner: OnItemClickListener) :
    ListAdapter<PromoCodeResponse.Data, PromoAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<PromoCodeResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: PromoCodeResponse.Data,
            newItem: PromoCodeResponse.Data
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: PromoCodeResponse.Data,
            newItem: PromoCodeResponse.Data
        ): Boolean {
            return oldItem.code == newItem.code &&
                    oldItem.price == newItem.price &&
                    oldItem.timesNum == newItem.timesNum &&
                    oldItem.isActive == newItem.isActive
        }

    }

    interface OnItemClickListener {
        fun onItemClick(data: PromoCodeResponse.Data)

    }

    class viewholder(var binding: PromoCodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: PromoCodeResponse.Data) {
            binding.txtCode.setText(data.code)
            binding.txtPrice.setText(data.price.toString())
            binding.txtNum.setText(data.timesNum.toString())
            binding.switchAvailable.isChecked = data.isActive == true
            binding.txtCodeContainer.isEnabled = false
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            PromoCodeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))


        holder.binding.root.setOnClickListener {
            listner.onItemClick(getItem(holder.absoluteAdapterPosition))
        }

    }
}