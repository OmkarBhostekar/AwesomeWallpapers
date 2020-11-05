package com.example.awesomewallpapers.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.hilt.Assisted
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.awesomewallpapers.data.repositories.MainRepository

class MainViewModel @ViewModelInject constructor(
    private val repository: MainRepository,
    @Assisted state: SavedStateHandle
) : ViewModel(){
    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val wallpapers = currentQuery.switchMap {queryString ->
        repository.getSearchWallpapers(queryString).cachedIn(viewModelScope)
    }

    fun searchWallpapers(query: String){
        currentQuery.value = query
    }

    companion object{
        const val CURRENT_QUERY = "current_query"
        const val DEFAULT_QUERY = "city"
    }
}