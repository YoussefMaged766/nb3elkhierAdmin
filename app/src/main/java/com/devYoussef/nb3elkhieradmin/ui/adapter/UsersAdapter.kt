package com.devYoussef.nb3elkhieradmin.ui.adapter

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.PromoCodeItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.UserBanItemBinding
import com.devYoussef.nb3elkhieradmin.databinding.UserItemBinding
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse
import com.devYoussef.nb3elkhieradmin.model.UsersResponse

class UsersAdapter() :
    ListAdapter<UsersResponse.User, UsersAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<UsersResponse.User>() {
        override fun areItemsTheSame(
            oldItem: UsersResponse.User,
            newItem: UsersResponse.User
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: UsersResponse.User,
            newItem: UsersResponse.User
        ): Boolean {
            return  oldItem.image?.url == newItem.image?.url &&
                    oldItem.userName == newItem.userName &&
                    oldItem.shopAddress == newItem.shopAddress &&
                    oldItem.shopName == newItem.shopName &&
                    oldItem.isBlocked == newItem.isBlocked
        }

    }



    class viewholder(var binding: UserItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: UsersResponse.User) {
            if (data.image?.url == null){
                binding.imgUserBan.setImageResource(R.drawable.img_default_user)
            } else{
                Glide.with(itemView).load(data.image.url).into(binding.imgUserBan)
            }

            binding.txtUserName.text = data.userName
            binding.txtUserShopName.text ="${data.shopName}, ${data.region}"
            binding.txtShopAddress.text = data.shopAddress
            binding.txtPhoneCountry.text = "${data.phone}, ${data.country}"

            if (data.ordersId.isNullOrEmpty()){
                binding.txtOrdersNumber.text ="عدد الفواتير:0 "
            } else{
                binding.txtOrdersNumber.text =" عدد الفواتير: ${ data.ordersId.size}"

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(getItem(position))


        holder.binding.root.setOnClickListener {
            if (holder.binding.hidingLayout.isVisible){
                TransitionManager.beginDelayedTransition(holder.binding.root , AutoTransition())
                holder.binding.hidingLayout.isVisible = false
            } else{
                TransitionManager.beginDelayedTransition(holder.binding.root , AutoTransition())
                holder.binding.hidingLayout.isVisible = true
            }
        }

    }
}