package com.devYoussef.nb3elkhieradmin.ui.adapter.paging


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.databinding.ProductItemBinding
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.model.dummyProduct

class ProductsPagingAdapter(private val listener: OnButton1ClickListener) :PagingDataAdapter<dummyProduct,ProductsPagingAdapter.viewholder>(Companion) {

    companion object : DiffUtil.ItemCallback<dummyProduct>() {
        override fun areItemsTheSame(
            oldItem: dummyProduct,
            newItem: dummyProduct
        ): Boolean {

            return oldItem.data._id == newItem.data._id
        }

        override fun areContentsTheSame(
            oldItem: dummyProduct,
            newItem: dummyProduct
        ): Boolean {
            return oldItem.data.image?.url == newItem.data.image?.url &&
                    oldItem.data.name == newItem.data.name &&
                    oldItem.data.isAvailable == newItem.data.isAvailable
        }

    }
    interface OnButton1ClickListener {
        fun onButtonEditClick(data: dummyProduct, binding: ProductItemBinding , position: Int)
        fun onButtonDeleteClick(data: dummyProduct, binding: ProductItemBinding)
    }


    inner class viewholder(var binding: ProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: dummyProduct) {

            Glide.with(binding.root).load(data.data.image?.url).into(binding.imgProduct)
            binding.txtProductName.text = data.data.name

            binding.overlay.isVisible = data.data.isAvailable != true
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