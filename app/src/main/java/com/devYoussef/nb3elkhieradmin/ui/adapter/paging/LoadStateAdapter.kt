package com.devYoussef.nab3elkheir.ui.adapter.paging

import android.view.ViewGroup
import androidx.paging.LoadState
import com.devYoussef.nb3elkhieradmin.ui.adapter.paging.LoadStateViewHolder

class LoadStateAdapter(private  val retry:()->Unit):androidx.paging.LoadStateAdapter<LoadStateViewHolder>(){
    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        return LoadStateViewHolder.create(parent, retry)
    }


}