package com.example.awesomewallpapers.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.awesomewallpapers.R
import com.example.awesomewallpapers.data.models.Photo
import com.example.awesomewallpapers.databinding.ItemWallpaperBinding

class WallpapersAdapter(
    private val listener: OnItemClickListener
) :
    PagingDataAdapter<Photo,WallpapersAdapter.WallpaperViewHolder>(WALLPAPER_COMPARATOR){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WallpaperViewHolder {
        val binding = ItemWallpaperBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WallpaperViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WallpaperViewHolder, position: Int) {
        val wallpaper = getItem(position)
        wallpaper?.let {
            holder.bind(it)
        }
    }

    companion object{
        val WALLPAPER_COMPARATOR = object : DiffUtil.ItemCallback<Photo>(){
            override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
                return oldItem == newItem
            }

        }
    }

    inner class WallpaperViewHolder(private val binding: ItemWallpaperBinding) :
        RecyclerView.ViewHolder(binding.root){

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    val item = getItem(position)
                    item?.let {
                        listener.onItemClick(it)
                    }
                }
            }
        }

        fun bind(photo: Photo){
            binding.apply {
                Glide.with(itemView)
                    .load(photo.src.medium)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_cancel)
                    .into(ivWallpaper)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClick(photo: Photo)
    }
}