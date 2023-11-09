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
import com.devYoussef.nb3elkhieradmin.databinding.UserBanItemBinding
import com.devYoussef.nb3elkhieradmin.model.BannerResponse
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse

class BannerAdapter(private var listner: OnItemClickListener) :
    ListAdapter<BannerResponse.Data, BannerAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<BannerResponse.Data>() {
        override fun areItemsTheSame(
            oldItem:BannerResponse.Data,
            newItem: BannerResponse.Data
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: BannerResponse.Data,
            newItem: BannerResponse.Data
        ): Boolean {
            return  oldItem.image == newItem.image
        }

    }

    interface OnItemClickListener {
        fun onItemClick(data: BannerResponse.Data)
    }

    class viewholder(var binding: BannerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BannerResponse.Data) {
           Glide.with(itemView).load(data.image?.url).into(binding.bannerImage)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            BannerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))

        holder.binding.root.setOnClickListener {
            listner.onItemClick(getItem(holder.absoluteAdapterPosition))
        }

    }
}