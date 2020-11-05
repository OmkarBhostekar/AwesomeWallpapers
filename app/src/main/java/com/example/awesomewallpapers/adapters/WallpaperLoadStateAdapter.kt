package com.example.awesomewallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.awesomewallpapers.databinding.WallpaperLoadStateFooterBinding

class WallpaperLoadStateAdapter(
        private val retry: ()->Unit
) : LoadStateAdapter<WallpaperLoadStateAdapter.WallpaperLoadStateViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): WallpaperLoadStateViewHolder {
        val binding = WallpaperLoadStateFooterBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WallpaperLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }


    inner class WallpaperLoadStateViewHolder(private val binding: WallpaperLoadStateFooterBinding)
        : RecyclerView.ViewHolder(binding.root){
        init {
            binding.btnRetry.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState){
            binding.apply {
                progressBar.isVisible = loadState is LoadState.Loading
                btnRetry.isVisible = loadState !is LoadState.Loading
                tvError.isVisible = loadState !is LoadState.Loading
            }
        }
    }


}