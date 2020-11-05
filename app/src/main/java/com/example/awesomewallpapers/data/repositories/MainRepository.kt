package com.example.awesomewallpapers.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.awesomewallpapers.api.PexelsApi
import com.example.awesomewallpapers.data.PexelsPagingSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepository @Inject constructor(
    private val pexelsApi: PexelsApi
) {
    fun getSearchWallpapers(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 40,
                maxSize = 200,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PexelsPagingSource(pexelsApi,query)}
        ).liveData
}