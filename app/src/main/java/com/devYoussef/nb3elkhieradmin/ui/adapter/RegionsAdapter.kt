package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.databinding.BannerItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.PromoCodeItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.RegionItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.UserBanItemBinding
import com.devYoussef.nb3elkhieradmin.model.BannerResponse
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse
import com.devYoussef.nb3elkhieradmin.model.RegionsResponse

class RegionsAdapter(private var listner: OnItemClickListener) :
    ListAdapter<RegionsResponse.Data, RegionsAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<RegionsResponse.Data>() {
        override fun areItemsTheSame(
            oldItem:RegionsResponse.Data,
            newItem: RegionsResponse.Data
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: RegionsResponse.Data,
            newItem: RegionsResponse.Data
        ): Boolean {
            return  oldItem.region == newItem.region&&
                    oldItem.limit == newItem.limit&&
                    oldItem.country == newItem.country
        }

    }

    interface OnItemClickListener {
        fun onEditItemClick(data: RegionsResponse.Data)
        fun onDeleteItemClick(data: RegionsResponse.Data)
    }

    class viewholder(var binding: RegionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RegionsResponse.Data) {
            binding.txtRegionName.text = data.region
            if (data.country=="مصر"){
                binding.txtMinOrder.text = data.limit.toString()+" ج.م"
            } else {
                binding.txtMinOrder.text = data.limit.toString()+" $"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            RegionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.imgDelete.setOnClickListener {
            listner.onDeleteItemClick(getItem(holder.absoluteAdapterPosition))
        }
        holder.binding.imgEdit.setOnClickListener {
            listner.onEditItemClick(getItem(holder.absoluteAdapterPosition))
        }

    }
}