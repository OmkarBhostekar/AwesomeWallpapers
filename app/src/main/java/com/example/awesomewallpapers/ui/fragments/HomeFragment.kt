package com.example.awesomewallpapers.ui.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.example.awesomewallpapers.R
import com.example.awesomewallpapers.adapters.WallpaperLoadStateAdapter
import com.example.awesomewallpapers.adapters.WallpapersAdapter
import com.example.awesomewallpapers.data.models.Photo
import com.example.awesomewallpapers.databinding.FragmentHomeBinding
import com.example.awesomewallpapers.ui.viewmodels.MainViewModel
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home),WallpapersAdapter.OnItemClickListener {

    private val viewModel by viewModels<MainViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    val tabTitles = listOf("City","Nature","Travel","Cars","Animals","Love","Motivation","Art")
    val tabBackgrounds = listOf(R.drawable.city,R.drawable.nature,R.drawable.travel,R.drawable.car,
            R.drawable.animals,R.drawable.love,R.drawable.motivation,R.drawable.art)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        val adapter = WallpapersAdapter(this)

        binding.apply {
            rvWallpapers.setHasFixedSize(true)
            rvWallpapers.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = WallpaperLoadStateAdapter{adapter.retry()},
                    footer = WallpaperLoadStateAdapter{adapter.retry()}
            )
            rvWallpapers.itemAnimator = null
            btnRetry.setOnClickListener {
                adapter.retry()
            }

            categoriesTabLayout.tabSelectedIndicator?.setVisible(false,true)
            for (i in tabTitles.indices){
                categoriesTabLayout.addTab(newTab(tabTitles[i],tabBackgrounds[i]))
            }
            val tab = categoriesTabLayout.getTabAt(0)
            tab?.select()
            categoriesTabLayout.addOnTabSelectedListener(tabSelectListener)
        }

        viewModel.wallpapers.observe(viewLifecycleOwner,{
            adapter.submitData(viewLifecycleOwner.lifecycle,it)
        })

        adapter.addLoadStateListener {loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                rvWallpapers.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1){
                    rvWallpapers.isVisible = false
                    tvResultsNotFound.isVisible = true
                }else{
                    tvResultsNotFound.isVisible = false
                }
            }
        }

        setHasOptionsMenu(true)
    }

    private fun newTab(s: String,bg:Int): TabLayout.Tab {
        val tab = binding.categoriesTabLayout.newTab()
        tab.view.setPadding(0,0,0,0)
        tab.setCustomView(R.layout.custom_tab)
        val tvTabName = tab.customView?.findViewById<TextView>(R.id.tvTabTitle)
        val ivBackground = tab.customView?.findViewById<ImageView>(R.id.ivBackgroundImage)
        ivBackground?.setColorFilter(Color.parseColor("#50000000"))
        Glide.with(requireActivity()).load(bg).into(ivBackground!!)
        tvTabName?.text = s
        return tab
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home,menu)

        val search = menu.findItem(R.id.miSearch)
        val searchView = search.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    binding.rvWallpapers.scrollToPosition(0)
                    viewModel.searchWallpapers(query)
                    searchView.clearFocus()
                    val selectedTabBg = binding.categoriesTabLayout.getTabAt(binding.categoriesTabLayout.selectedTabPosition)
                            ?.customView?.findViewById<ImageView>(R.id.ivBackgroundImage)
                    selectedTabBg?.let {
                        it.setColorFilter(Color.parseColor("#40ffffff"))
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val tabSelectListener = object : TabLayout.OnTabSelectedListener{
        override fun onTabSelected(tab: TabLayout.Tab?) {
            val ivBackground = tab?.customView?.findViewById<ImageView>(R.id.ivBackgroundImage)
            ivBackground?.let {
                it.setColorFilter(Color.parseColor("#40ffffff"))
            }
            viewModel.searchWallpapers(tabTitles[tab!!.position])
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            val ivBackground = tab?.customView?.findViewById<ImageView>(R.id.ivBackgroundImage)
            ivBackground?.let {
                it.setColorFilter(Color.parseColor("#50000000"))
            }
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }
    }

    override fun onItemClick(photo: Photo) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(photo))
    }
}