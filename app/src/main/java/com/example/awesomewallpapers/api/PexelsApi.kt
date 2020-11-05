package com.example.awesomewallpapers.api

import com.example.awesomewallpapers.BuildConfig
import com.example.awesomewallpapers.data.models.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface PexelsApi {

    companion object {
        const val BASE_URL = "https://api.pexels.com/v1/"
        const val API_KEY = BuildConfig.PEXELS_API_KEY
    }

    @GET("search")
    suspend fun searchWallpapers(
        @Header("Authorization") header: String,
        @Query("query") query: String,
        @Query("per_page") per_page: Int,
        @Query("page") page: Int
    ) : SearchResponse
}