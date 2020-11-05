package com.example.awesomewallpapers.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Photo(
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    val photographer_id: Int,
    val photographer_url: String,
    val src: Src,
    val url: String,
    val width: Int
) : Parcelable