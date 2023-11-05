package com.devYoussef.nb3elkhieradmin.ui.adapter.paging

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.PagingLoadBinding


class LoadStateViewHolder(private val binding: PagingLoadBinding,
                          retry:() -> Unit):RecyclerView.ViewHolder(binding.root){

    init {
        binding.retryButtonItem.setOnClickListener {
            retry.invoke()
        }
    }
    fun bind(loadState: LoadState){
        if (loadState is LoadState.Error){
            Log.e("bind: ","alo" )
            binding.errorMsg.text = "حاول مرة اخرى لاحقا"
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButtonItem.isVisible = loadState !is LoadState.Loading
        binding.errorMsg.isVisible = loadState !is LoadState.Loading
    }
    companion object{
        fun create(parent : ViewGroup, retry: () -> Unit): LoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.paging_load,parent,false)
            val binding = PagingLoadBinding.bind(view)
            return LoadStateViewHolder(binding,retry)
        }
    }


}