package com.example.awesomewallpapers.data

import androidx.paging.PagingSource
import com.example.awesomewallpapers.api.PexelsApi
import com.example.awesomewallpapers.data.models.Photo
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_INDEX = 1

class PexelsPagingSource(
    private val pexelsApi: PexelsApi,
    private val query: String
) : PagingSource<Int,Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val position = params.key ?: STARTING_INDEX

        return try {
            val response = pexelsApi.searchWallpapers(PexelsApi.API_KEY,query,params.loadSize,position)
            val photos = response.photos
            LoadResult.Page(
                data = photos,
                prevKey = if (position== STARTING_INDEX) null else position-1,
                nextKey = if (photos.isEmpty()) null else position+1
            )
        }catch (e: IOException){
            LoadResult.Error(e)
        }catch (e: HttpException){
            LoadResult.Error(e)
        }
    }
}