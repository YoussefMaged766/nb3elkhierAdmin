package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.databinding.PromoCodeItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.UserBanItemBinding
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse

class UserBanAdapter(private var listner: OnItemClickListener) :
    ListAdapter<BlockUsersResponse.User, UserBanAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<BlockUsersResponse.User>() {
        override fun areItemsTheSame(
            oldItem: BlockUsersResponse.User,
            newItem: BlockUsersResponse.User
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: BlockUsersResponse.User,
            newItem: BlockUsersResponse.User
        ): Boolean {
            return  oldItem.image == newItem.image &&
                    oldItem.userName == newItem.userName &&
                    oldItem.shopAddress == newItem.shopAddress &&
                    oldItem.shopName == newItem.shopName &&
                    oldItem.isBlocked == newItem.isBlocked
        }

    }

    interface OnItemClickListener {
        fun onItemClick(data: BlockUsersResponse.User)

    }

    class viewholder(var binding: UserBanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: BlockUsersResponse.User) {
           Glide.with(itemView).load(data.image?.url).into(binding.imgUserBan)
            binding.txtUserNameBan.text = data.userName
            binding.txtUserShopNameBan.text = data.shopName

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            UserBanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))


        holder.binding.txtUnBan.setOnClickListener {
            listner.onItemClick(getItem(holder.absoluteAdapterPosition))
        }

    }
}