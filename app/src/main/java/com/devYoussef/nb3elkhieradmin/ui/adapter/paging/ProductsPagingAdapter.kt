package com.devYoussef.nb3elkhieradmin.ui.adapter.paging


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.databinding.ProductItemBinding
import com.devYoussef.nb3elkhieradmin.model.ProductResponse

class ProductsPagingAdapter(private val listener: OnButton1ClickListener) :PagingDataAdapter<ProductResponse.Data,ProductsPagingAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<ProductResponse.Data>() {
        override fun areItemsTheSame(
            oldItem: ProductResponse.Data,
            newItem: ProductResponse.Data
        ): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: ProductResponse.Data,
            newItem: ProductResponse.Data
        ): Boolean {
            return oldItem.image?.url == newItem.image?.url &&
                    oldItem.name == newItem.name
        }

    }
    interface OnButton1ClickListener {
        fun onButtonEditClick(data: ProductResponse.Data, binding: ProductItemBinding , position: Int)
        fun onButtonDeleteClick(data: ProductResponse.Data, binding: ProductItemBinding)
    }


    inner class viewholder(var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ProductResponse.Data) {

            Glide.with(binding.root).load(data.image?.url).into(binding.imgProduct)
            binding.txtProductName.text = data.name

        }



    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        if (position < itemCount) {
            val item = getItem(position)
            if (item != null) {
                holder.bind(item)

                holder.binding.btnEditProduct.setOnClickListener {
                    listener.onButtonEditClick(item, holder.binding , position)
                }

                holder.binding.btnDeleteProduct.setOnClickListener {
                    listener.onButtonDeleteClick(item, holder.binding)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val binding =
            ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }
}