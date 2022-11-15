package com.francotte.musicplayer4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.RequestManager
import com.francotte.musicplayer4.adapters.SwipeSongAdapter

import com.francotte.musicplayer4.data.entities.Song
import com.francotte.musicplayer4.databinding.ActivityMainBinding
import com.francotte.musicplayer4.exoplayer.toSong
import com.francotte.musicplayer4.other.Status
import com.francotte.musicplayer4.ui.fragments.HomeFragment
import com.francotte.musicplayer4.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var glide: RequestManager

    @Inject
    lateinit var swipeSongAdapter: SwipeSongAdapter



    private var curPlayingSong: Song? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    subscribeToObservers()

       binding.vpSong.adapter = swipeSongAdapter


   val homeFragment = HomeFragment()
     val fragmentManager: FragmentManager = supportFragmentManager
      val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
       fragmentTransaction.replace(R.id.flFragmentContainer, homeFragment).commit()

    }

   private fun switchViewPagerToCurrentSong(song: Song) {
        val newItemIndex = swipeSongAdapter.songs.indexOf(song)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            curPlayingSong = song
        }
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> result.data?.let { songs ->
                        swipeSongAdapter.songs = songs
                        if(songs.isNotEmpty()) {
                         glide.load((curPlayingSong ?: songs[0]).imageUrl).into(binding.ivCurSongImage)
                        }
                        switchViewPagerToCurrentSong(curPlayingSong ?: return@observe)
                    }
                        Status.ERROR -> Unit
                    Status.LOADING -> Unit
                }
            }
        }
        mainViewModel.curPlayingSong.observe(this) {
            if(it == null) return@observe
            curPlayingSong = it.toSong()
            glide.load(curPlayingSong?.imageUrl).into(binding.ivCurSongImage)
          //  switchViewPagerToCurrentSong(curPlayingSong ?: return@observe)
        }
    }

}