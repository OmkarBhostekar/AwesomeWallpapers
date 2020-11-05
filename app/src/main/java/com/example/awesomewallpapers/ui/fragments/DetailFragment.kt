package com.example.awesomewallpapers.ui.fragments

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.awesomewallpapers.R
import com.example.awesomewallpapers.databinding.FragmentDetailBinding


class DetailFragment : Fragment(R.layout.fragment_detail) {

    private val args by navArgs<DetailFragmentArgs>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDetailBinding.bind(view)

        val photo = args.photo
        var photoBitmap: Bitmap? = null

        Glide.with(view)
                .asBitmap()
                .load(photo.src.large2x)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        photoBitmap = resource
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                })

        Glide.with(view).load(photo.src.portrait).into(binding.ivWallpaper)

        binding.btnSetWallpaper.setOnClickListener {
            binding.progressBar.isVisible = true
            photoBitmap?.let {
                WallpaperManager.getInstance(activity).setBitmap(photoBitmap,null,true,WallpaperManager.FLAG_SYSTEM)
                binding.progressBar.isVisible = false
                Toast.makeText(activity, "Home screen wallpaper changed", Toast.LENGTH_SHORT).show()
                startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            }

        }

        binding.btnSetLockscreen.setOnClickListener {
            binding.progressBar.isVisible = true
            photoBitmap?.let {
                WallpaperManager.getInstance(activity).setBitmap(photoBitmap,null,true,WallpaperManager.FLAG_LOCK)
                binding.progressBar.isVisible = false
                Toast.makeText(activity, "Lock screen wallpaper changed", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TITLE,"")
            shareIntent.putExtra(Intent.EXTRA_SUBJECT,"")
            shareIntent.putExtra(Intent.EXTRA_TEXT,"Hey, Download this awesome wallpaper by clicking this link - ${photo.url}")
            startActivity(Intent.createChooser(shareIntent,"Share Using"))
        }

    }
}